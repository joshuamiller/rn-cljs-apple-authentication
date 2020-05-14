(ns io.joshmiller.rn-cljs-apple-authentication.core
  (:require [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [cljs.spec.alpha :as s]
            [clojure.set :refer [rename-keys map-invert]]
            ["@invertase/react-native-apple-authentication"]
            [react-native :as rn]))

(def auth-errors
  {:unknown "1000"
   :canceled "1001"
   :invalid-response "1002"
   :not-handled "1003"
   :failed "1004"})

(def request-operations
  {:implicit 0
   :login 1
   :refresh 2
   :logout 3})

(def request-scopes
  {:email 0
   :full-name 1})

(def real-user-status
  {:unsupported 0
   :unknown 1
   :likely-real 2})

(def credential-state
  {:revoked 0
   :authorized 1
   :not-found 2
   :transferred 3})

(def native-module
  (.-RNAppleAuthModule rn/NativeModules))

(def emitter
  (rn/NativeEventEmitter. native-module))

(defn is-supported?
  []
  (and native-module (.-isSupported native-module)))

(defn is-signup-button-supported?
  []
  (and native-module (.-isSignUpButtonSupported native-module)))

(defn throw-if-unsupported
  []
  (when (not (is-supported?))
    (throw (ex-info "AppleAuth is not supported on this device" {}))))

(defn get-credential-state-for-user
  [user]
  (.getCredentialStateForUser native-module user))

(defn request-options->js
  [opts]
  (-> opts
      (update ::requested-operation request-operations)
      (update ::requested-scopes (partial map request-scopes))
      (rename-keys {::requested-operation :requestedOperation
                    ::requested-scopes :requestedScopes})
      clj->js))

(s/def ::requested-operation (-> request-operations keys set))
(s/def ::requested-scopes (s/coll-of (-> request-scopes keys set)))
(s/def ::request-options (s/keys :req [::requested-operation]
                                 :opt [::requested-scopes]))

(defn perform-request
  [request-options]
  (throw-if-unsupported)

  (when (not (s/valid? ::request-options request-options))
    (throw (ex-info "Invalid request options"
                    (s/explain-data ::request-options request-options))))

  (.performRequest native-module (request-options->js request-options)))

(defn on-credential-revoked
  [listener]
  (throw-if-unsupported)

  (let [subscription (.addListener emitter "RNAppleAuth.onCredentialRevoked" listener)]
    (.-remove subscription)))

(defn basic-login
  []
  (go
    (try
      (let [request-opts {::requested-operation :login
                          ::requested-scopes [:email :full-name]}
            response (js->clj (<p! (perform-request request-opts))
                              :keywordize-keys true)
            user (.-user response)
            cred-state (<p! (get-credential-state-for-user user))]
        (println "User" user)
        (println (get (map-invert credential-state) cred-state))
        (if (= cred-state (:authorized credential-state))
          user
          {:error (get (map-invert credential-state) cred-state)}))
      (catch js/Error err
        (println err)
        (println (ex-cause err))))))

(ns io.joshmiller.rn-cljs-apple-authentication.ui
  (:require ["@invertase/react-native-apple-authentication" :as auth]
            [reagent.core :as r]))

(def button-styles
  {:default "White"
   :white "White"
   :white-outline "WhiteOutline"
   :black "Black"})

(def button-types
  {:default "SignIn"
   :sign-in "SignIn"
   :continue "Continue"
   :sign-up "SignUp"})

(def apple-button (r/adapt-react-class auth/AppleButton))

(defn sign-in-with-apple-button
  [opts]
  (let [{:keys [button-type button-style]} opts
        button-type*  (get button-types (or button-type :default))
        button-style* (get button-styles (or button-style :default))
        opts* (assoc (or opts {})
                     :button-type button-type*
                     :button-style button-style*)]
    (fn []
      [apple-button opts*])))

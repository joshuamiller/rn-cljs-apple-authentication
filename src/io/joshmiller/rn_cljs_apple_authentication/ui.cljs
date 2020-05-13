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

(def sign-in-with-apple-button (r/adapt-react-class auth/AppleButton))

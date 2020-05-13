# Sign In With Apple on React Native with ClojureScript

`rn-cljs-apple-authentication` is a library that adapts the React Native
bindings to [Sign In With Apple](https://developer.apple.com/sign-in-with-apple/
https://developer.apple.com/sign-in-with-apple/)
provided by [`@invertase/react-native-apple-authentication`](https://github.com/invertase/react-native-apple-authentication)
to a more fluent Clojure idiom.

## Library Status

This is pre-release software that I am using as I develop an application
that uses it, and will change frequently until release.

## Installation

Add the following to your `deps.edn`:

```clojure
io.joshmiller/rn-cljs-apple-authentication {:git/url "https:github.com/joshuamiller/rn-cljs-apple-authentication"
                                            :sha "current-sha"}
```

Install dependencies:

`clj -m cljs.main --install-deps`

Compile provided Cocoapods:

```bash
cd ios/
pod install
cd ..
```

## Usage

In your application:

```clojure
(ns my-app
  (:require [io.joshmiller.rn-cljs-apple-authentication.ui :as auth-ui]
            [io.joshmiller.rn-cljs-apple-authentication.core :as auth]))

,,,

[auth-ui/sign-in-with-apple-button
  {:button-type (:default auth-ui/button-types)
   :button-style (:default auth-ui/button-styles)
   :on-press auth/basic-login}]
```

The `basic-login` fn will return a `core.async` channel that will
deliver either a user object or an error depending on the outcome of
the sign in process. Note that it will always return an error in the
simulator. You will likely want to adapt the code in `basic-login`
to fit your application's flow.

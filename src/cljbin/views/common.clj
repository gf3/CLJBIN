(ns cljbin.views.common
  (:use hiccup.core
        hiccup.form-helpers
        hiccup.page-helpers
        ring.middleware.anti-forgery)
  (:require [ring.util.response :as response]))

(def ^{:private true} css
  "@font-face {
    font-family: 'HomesteadRegular';
    src: url('/fonts/homestead-regular-webfont.eot');
    src: url('/fonts/homestead-regular-webfont.eot?#iefix') format('embedded-opentype'),
         url('/fonts/homestead-regular-webfont.woff') format('woff'),
         url('/fonts/homestead-regular-webfont.ttf') format('truetype'),
         url('/fonts/homestead-regular-webfont.svg#HomesteadDisplay') format('svg');
    font-weight: normal;
    font-style: normal;
  }")

(defn layout [& content]
  (response/response
    (html5 {:lang "en-CA"}
           [:head
            [:meta {:charset "utf8"}]
            [:meta {:content "IE=edge,chrome=1" :http-equiv "X-UA-Compatible"}]
            [:meta {:content "width=device-width, initial-scale=1" :name "viewport"}]
            [:title "cljbin"]
            [:style css]
            (include-css "/css/normalize.css")
            (include-css "/css/cljbin.css")
            (include-css "http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,600")
            ]
           [:body
            [:section#paste
             [:header
              [:h1 "Clojure Bin"]
              ]
             content
             ]
            [:footer
             [:p "&#955; Clojure Bin."]]])))

(defn hidden-csrf-field []
  (hidden-field '__anti-forgery-token *anti-forgery-token*))


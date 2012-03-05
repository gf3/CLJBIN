(ns cljbin.views.common
  (:use hiccup.core
        hiccup.form-helpers
        hiccup.page-helpers
        ring.middleware.anti-forgery)
  (:require [ring.util.response :as response]))

(def ^{:private true} css
  "@font-face {
    font-family: 'SegoeUINormal';
    src: url('/fonts/segoeui-webfont.eot');
    src: url('/fonts/segoeui-webfont.eot?#iefix') format('embedded-opentype'),
         url('/fonts/segoeui-webfont.woff') format('woff'),
         url('/fonts/segoeui-webfont.ttf') format('truetype'),
         url('/fonts/segoeui-webfont.svg#SegoeUINormal') format('svg');
    font-weight: normal;
    font-style: normal;
  }")

(def ^{:private true} gajs
  "var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-319181-10']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();")

(defn layout [& content]
  (response/response
    (html5 {:lang "en-CA"}
           [:head
            [:meta {:charset "utf8"}]
            [:meta {:content "IE=edge,chrome=1" :http-equiv "X-UA-Compatible"}]
            [:meta {:content "width=device-width, initial-scale=1" :name "viewport"}]
            [:title "cljbin"]
            [:script gajs]
            [:style css]
            (include-css "http://fonts.googleapis.com/css?family=Droid+Sans+Mono")
            (include-css "/css/normalize.css")
            (include-css "/css/cljbin.css")
            (include-css "/css/media_queries.css")
            (include-css "/css/shThemeTomorrow.css")
            (include-css "/css/shClojureExtra.css")
            ]
           [:body
            [:header {:role "banner"} 
             (link-to "/" [:h1 "cljbin"])]
            [:section#paste
             content]
            [:footer
             [:p
              "&#955; Clojure Paste Bin with Evaluation. "
              (link-to "https://github.com/gf3/CLJBIN" "Source on GitHub")
              "."
              ]]
            (include-js "/js/vendor/zepto.min.js")
            (include-js "/js/vendor/shCore.js")
            (include-js "/js/vendor/shBrushClojure.js")
            (include-js "/js/main.js")])))

(defn hidden-csrf-field []
  (hidden-field '__anti-forgery-token *anti-forgery-token*))


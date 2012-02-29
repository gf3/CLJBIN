(ns cljbin.views.paste-views
  (:use (ciste [views :only [defview]])
        (clj-time [core :only [in-days interval now]])
        (clj-time [coerce :only [from-date]])
        cljbin.actions.paste-actions
        cljbin.views.common
        hiccup.core
        hiccup.form-helpers 
        hiccup.page-helpers)
  (:require [ring.util.response :as response]))

(defview #'index :html
  [request _]
  (layout
    (form-to [:put "/paste"]
             ; (hidden-csrf-field)

             [:div.code
              [:textarea#code {:name "code" :placeholder "(paste :clojure \"code\")"}]]

             [:ul.actions
              [:li.hidden [:a {:href "#"} "Run"]]
              [:li (submit-button "Paste")]])
    ))

(defview #'show :html
  [request paste]
  (layout
    (form-to [:put "/paste"]
             ; (hidden-csrf-field)
             (hidden-field 'fork-of (:_id paste))

             [:div.code
              [:textarea#code.hidden {:name "code" :placeholder "(paste :clojure \"code\")"} (:code paste)] 
              [:pre {:class "brush: clojure"}
               (escape-html (:code paste))]]

             [:ul.output
              (for [out (:output paste)]
                [:li
                 [:pre (escape-html out)]])]

             [:p.meta
              "Pasted "
              (in-days (interval (from-date (:created-at paste)) (now)))
              " days ago. "
              (if (:expires-at paste)
                [:span.muted
                 "Expires in "
                 (in-days (interval (now) (from-date (:expires-at paste))))
                 " days."])]

             (if (:fork-of paste)
               [:div#fork-of
                [:p
                 "Fork of "
                 (link-to (str "/paste/" (:fork-of paste)) (escape-html (str "#<Paste " (:fork-of paste) ">")))]])

             [:ul.actions
              [:li.paste-action.hidden (submit-button "Paste")]  
              [:li.fork-action [:a {:href "#"} "Fork"]]])
     ))

(defview #'put :html
  [request paste]
  (response/redirect-after-post (format "/paste/%s" (:_id paste))))

(defview #'delete :html
  [request paste]
  {:headers {"Location" "/"}
   :status 303
   :template false})


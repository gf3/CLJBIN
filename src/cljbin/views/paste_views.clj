(ns cljbin.views.paste-views
  (:use (ciste [views :only [defview]])
        (clj-time [core :only [in-days interval now]])
        (clj-time [coerce :only [from-date]])
        cljbin.actions.paste-actions
        cljbin.views.common
        hiccup.core
        hiccup.form-helpers)
  (:require [ring.util.response :as response]))

(defview #'index :html
  [request _]
  (layout
    [:p.meta "Pasted over 4 days ago. <span class=muted>Expires in 3 weeks.</span>"]
    (form-to [:put "/paste"]
             ; (hidden-csrf-field)

             [:div.code
              (text-area 'code "Paste")]

             [:ul.actions
              [:li (submit-button "Run")]
              [:li (submit-button "Paste")]])
    ))

(defview #'show :html
  [request paste]
  (layout
    [:p.meta
     "Pasted "
     (in-days (interval (from-date (:created-at paste)) (now)))
     " days ago. "
     (if (:expires-at paste)
       [:span.muted
        "Expires in "
        (in-days (interval (now) (from-date (:expires-at paste))))
        " days."])
     ]
    (form-to [:put "/paste"]
             ; (hidden-csrf-field)
             (hidden-field 'fork-of (:_id paste))

             [:div.code
              [:pre
               [:code
                (:code paste)]]]

             [:ul.output
              (for [out (:output paste)]
                [:li
                 [:pre out]])]

             [:ul.actions
              [:li (submit-button "Fork")]])
     ))

(defview #'put :html
  [request paste]
  (response/redirect-after-post (format "/paste/%s" (:_id paste))))

(defview #'delete :html
  [request paste]
  {:headers {"Location" "/"}
   :status 303
   :template false})


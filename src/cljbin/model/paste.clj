(ns cljbin.model.paste
  (:require [cljbin.eval :as eval]
            [somnium.congomongo :as cm])
  (:import java.util.Date))

(def ^{:private true} valid-keys [:code :output :fork-of :session :created-at :updated-at])

(defn create! [paste]
  (if paste
    (if (:code paste)
      (cm/insert! :pastes (assoc (select-keys paste valid-keys)
                                 :output (eval/run (:code paste))
                                 :created-at (Date.)
                                 :updated-at (Date.)))
      (throw (IllegalArgumentException. "Pastes must contain code")))
    (throw (IllegalArgumentException. "Can not create nil pastes"))))

(defn fetch-by-id [id]
  (cm/fetch-by-id :pastes (cm/object-id id)))


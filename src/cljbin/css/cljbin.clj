(ns cljbin.css.cljbin
  (:use com.evocomputing.colors
        cssgen)
  (:require [clojure.string :as string]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Util
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn comma-sep [col]
  (string/join ", " (map #(to-css %) col)))

(def vendors ["-moz-" "-ms-" "-o-" "-webkit-"])

(defn vendorify [prop value]
  (flatten 
    (map #(list (str % (name prop)) value)
         (concat vendors '("")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; URL Helpers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn url
  "Wraps a URL or path to make it fit for use with CSS."
  [url]
  {:pre  [(string? url)]
   :post [(string? %)]}
  (str "url(" url ")"))

; TODO Make configurable
(defn font-url
  "Generate a path to a font relative to the project's font directory."
  [img]
  {:pre  [(string? img)]
   :post [(string? %)]}
  (url (str "/fonts" img)))

; TODO Make configurable
(defn image-url
  "Generate a path to an image relative to the project's image directory."
  [img]
  {:pre  [(string? img)]
   :post [(string? %)]}
  (url (str "/images/" img)))

; TODO Make configurable
(defn stylesheet-url
  "Generate a path to a stylesheet relative to the project's stylesheet directory."
  [img]
  {:pre  [(string? img)]
   :post [(string? %)]}
  (url (str "/css" img)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; CSS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn background-clip
  "Set the background-clip property for all vendors, clip must be one of
  [:border-box :padding-box :content-box]."
  [clip]
  {:pre  [(keyword? clip)
          (some #{clip} [:border-box :padding-box :content-box])]
   :post [(seq? %)]}
  (vendorify :background-clip clip))

(defn box-shadow
  "Set the box-shadow property for all vendors."
  [& shadows]
  {:pre  [(every? vector? shadows)]
   :post [(seq? %)]}
  (vendorify :box-shadow
             (comma-sep shadows)))

(defn box-sizing
  "Set the box-sizing property for all vendors, sizing must be one of
  [:content-box :border-box :padding-box]."
  [sizing]
  {:pre  [(keyword? sizing)
          (some #{sizing} [:border-box :padding-box :content-box])]
   :post [(seq? %)]}
  (vendorify :box-sizing sizing))

(defn text-shadow
  "Generate text shadows."
  [& shadows]
  {:pre  [(every? vector? shadows)]
   :post [(seq? %)]}
  (list :text-shadow
        (comma-sep shadows)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Images
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn linear-gradient
  "Set the background-image using linear gradients for all vendors."
  [& gradients]
  {:pre  [(every? vector? gradients)]
   :post [(seq? %)]}
  (flatten
    (let [grads (map comma-sep gradients)]
      (for [vendor (concat vendors '(""))
            :let [value (map #(str vendor "linear-gradient(" % ")") grads)]]
        (list :background-image (comma-sep value))))))

(defn mask-image
  "Set the mask-image for all vendors."
  [img]
  (vendorify :mask-image (image-url img)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Border Radius
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn border-radius
  "Set the border-radius for all vendors."
  [radius]
  (vendorify :border-radius radius))


(defn border-corner-radius [vert horz radius]
  (let [vert (to-css vert)
        horz (to-css horz)]
    (flatten (concat (list (str "-moz-radius-" vert horz) radius)
                     (map #(list (str % vert "-" horz "-" radius) radius)
                          (drop-while #{"-moz-"} (concat vendors '(""))))))))

(defn border-top-left-radius
  "Set the top-left border-radius."
  [radius]
  (border-corner-radius :top :left radius))

(defn border-top-right-radius
  "Set the top-right border-radius."
  [radius]
  (border-corner-radius :top :right radius))

(defn border-bottom-left-radius
  "Set the bottom-left border-radius."
  [radius]
  (border-corner-radius :bottom :left radius))

(defn border-bottom-right-radius
  "Set the bottom-right border-radius."
  [radius]
  (border-corner-radius :top :left radius))

(defn border-top-radius
  "Set the top border-radius."
  [radius]
  (border-corner-radius :top :left  radius)
  (border-corner-radius :top :right radius))

(defn border-right-radius
  "Set the right border-radius."
  [radius]
  (border-corner-radius :top    :right radius)
  (border-corner-radius :bottom :left  radius))

(defn border-bottom-radius
  "Set the bottom border-radius."
  [radius]
  (border-corner-radius :bottom :left  radius)
  (border-corner-radius :bottom :right radius))

(defn border-left-radius
  "Set the left border-radius."
  [radius]
  (border-corner-radius :top    :left radius)
  (border-corner-radius :bottom :left radius))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Color
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn hsl
  ([h s l]
   (hsl (create-color {:h h :s s :l l})))
  ([color]
   (let [color (:hsl color)]
     (str "hsl("
          (color 0)
          ","
          (color 1)
          "%,"
          (color 2)
          "%)"))))

(defn hsla
  ([h s l a]
   (hsla (create-color {:h h :s s :l l :a (Math/round (* 255.0 a))})))
  ([color]
   (let [c (:hsl color)]
     (str "hsla("
          (c 0)
          ","
          (c 1)
          "%,"
          (c 2)
          "%,"
          (/ (alpha color) 255.0)
          ")"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Base
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def ^:dynamic *base*
  {:font-family "'Open Sans', 'Lucida Grande', sans-serif"
   :font-size 16
   :font-weight 600
   :color       (create-color {:h 0   :s 0  :l 27})
   :accent      (create-color {:h 216 :s 38 :l 56})
   :accent-two  (create-color "#FFC660")
   })

; Vertical Rythem
(def vr (* 1.3 (:font-size *base*)))

(defn font-family []
  (list :font-family (:font-family *base*)))

(defn font-size
  ([]
   (font-size 1))
  ([factor]
   (list :font-size (px (* factor (:font-size *base*))))))

(defn font-weight []
  (list :font-weight (:font-weight *base*)))

(defn line-height
  ([]
   (line-height 1))
  ([factor]
   (list :line-height (px (* factor vr)))))

; (defn font []
  ; (list :font (string/join " " (list (:font-weight *base*)
                                     ; (str (font-size) "/" (line-height))
                                     ; (:font-family *base*)))))

(defn homestead-regular []
  (list :font-family "'HomesteadRegular', sans-serif"
        :font-style "normal"
        :font-weight "normal"))

(defn open-sans []
  (list :font-family "'Open Sans', 'Lucida Grande', sans-serif"
        :font-style "normal"
        :font-weight 400))

(defn link-color []
  (list :color (hsl (darken (:accent *base*) 60))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Styles
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn -main []
  (css
    ; Base
    [:html
     ; :background-color :#3A3A3A
     ]

    [:body
     :margin [0 :auto]
     :width :960px
     :background-color :#3A3A3A
     :background-color :#E7E7E7
     :background-color :#A5A5A5
     :background-image (image-url "paint.png")
     :background-repeat :no-repeat
     :background-position [:center :top]
     :color (hsl (:color *base*))
     (font-family)
     (font-size)
     (font-weight)
     (line-height)
     ]

    ["p, ul, ol, dl, pre"
     :margin-top    (px vr)
     :margin-bottom (px vr)
     ]

    [:code
     :color :#E8E8E8
     ]

    [:div.code
     [:pre
      :padding [(px (/ vr 2.0)) (px vr)]
      :width "100%"
      :background-color (hsla 0 0 0 0.2)
      (linear-gradient [:bottom (hsla 0 0 20 0.4) (hsla 0 0 15 0.4)])
      (background-clip :padding-box)
      (box-shadow [0 :1px (hsla 0 0 100 0.2)] [:inset 0 0 :3px :2px :#000])
      (box-sizing :border-box)
      :color :#FFF
      :font-family "'Menlo', 'Menlo-Regular', 'Deja Vu Sans Mono', 'Inconsolata', Monaco, monospace"
      ]
     ]

    ; Headings
    ["h1, h2, h3, h4, h5, h6"
     :font-weight 600
     :color :#F3F2F2
     ]

    [:h1
     (font-size 1.8)
     (line-height 3)
     ]

    [:h2
     ]

    ; Header
    [:header
      ]

    ; Paste
    [:section#paste
     :margin-top (px (* 2 vr))
     :padding 0
     :background-color :#1B1B1B
     :background-image (image-url "bkg.png")
     (border-radius :8px)
     (box-shadow [0 :1px :3px (hsla 0 0 0 0.6)] [:inset 0 :2px (hsla 0 0 100 0.3)] [:inset 0 :-2px 0 :#1B1B1B])
     (text-shadow [0 :1px :2px :#000])
     :overflow :hidden

     [:header
      :height (px (* vr 3))
      (border-top-radius :8px)
      :background-color (hsla 0 0 40 0.7)
      (linear-gradient [:top (hsla 0 0 40 0.7) (hsla 0 0 15 0.7)])
      (box-shadow [:inset 0 :1px (hsla 0 0 100 0.5)] [:inset 0 :-2px 0 :#111])

      [:h1
       :margin 0
       :text-align :center
       (text-shadow [0 :-1px :#FFF] [0 :2px :3px :#000])
       ]
      ]

     [:p
      :padding [0 (px vr)]
      :color :#FFF
      ]
     ]

    [:form
     :margin-top (px vr)

     [:textarea
      :display :block
      :padding [(px (/ vr 2.0)) (px vr)]
      :width "100%"
      :min-height :350px
      :border 0
      (border-radius :3px)
      :background-color (hsla 0 0 0 0.2)
      (linear-gradient [:bottom (hsla 0 0 20 0.4) (hsla 0 0 15 0.4)])
      (background-clip :padding-box)
      (box-shadow [0 :1px (hsla 0 0 100 0.2)] [:inset 0 0 :3px :2px :#000])
      (box-sizing :border-box)
      :color :#FFF
      :font-family "'Menlo', 'Menlo-Regular', 'Deja Vu Sans Mono', 'Inconsolata', Monaco, monospace"
      :resize :none
      ]

     ["input[type=submit], button"
      :padding [0 (px (/ vr 2))]
      :background-color (hsla 0 0 40 0.7)
      (linear-gradient [:top (hsla 0 0 60 0.7) (hsla 0 0 30 0.7)])
      :border [:2px :solid (hsl 0 0 20)]
      (box-shadow [0 :1px :#FFF] [:inset 0 :-1px (hsla 0 0 100 0.1)] [:inset 0 :2px (hsla 0 0 40 0.7)] [:inset 0 :3px (hsla 0 0 100 0.7)])
      (border-radius :8px)
      (box-sizing :border-box)
      :color :#FFF
      (font-size 1.1)
      (line-height 2)
      (text-shadow [0 :1px :2px :#000])
      :-webkit-appearance :none
      ]
     ]

    ; Output
    [:ul.output
     :color :#E8E8E8
     ]

    ; Actions
    [:ul.actions
     :margin 0
     :padding (px vr)
     :list-style-type :none
     :background-color :#F7F7F7
     (linear-gradient [:bottom "hsl(240, 14%, 90%)" "hsla(240, 14%, 90%, 0) 30px"])
     (box-shadow [:inset 0 :2px (hsl 0 0 100)])
     :overflow :hidden

     [:li
      :display :inline
      :margin 0
      :padding 0
      :float :right
      ]
     ]

    ; Muted
    [:.muted
     :color (hsl 0 0 10)
     (text-shadow [0 :-1px :#000] [0 :1px (hsla 0 0 100 0.1)])
     ]
    
    ))


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
(defn transition-delay
  [& delays]
  (vendorify :transition-delay
             (comma-sep delays)))

(defn transition-duration
  [& durations]
  (vendorify :transition-duration
             (comma-sep durations)))

(defn transition-property
  [& properties]
  (vendorify :transition-property
             (comma-sep properties)))

(defn transition-timing-function
  [& functions]
  (vendorify :transition-timing-function
             (comma-sep functions)))

(defn transition
  [& transitions]
  (vendorify :transition
             (comma-sep transitions)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Base
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def ^:dynamic *base*
  {:font-family "MyriadPro-Regular, 'Myriad Pro Regular', MyriadPro, 'Myriad Pro', Calibri, sans-serif"
   :font-size   14
   :font-weight "normal"
   :color       (create-color {:h 80  :s 2  :l 34})
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

(defn segoe-ui []
  (list :font-family "'SegoeUINormal', sans-serif"
        :font-style  "normal"
        :font-weight "normal"))

(defn myriad []
  (list :font-family "MyriadPro-Regular, 'Myriad Pro Regular', MyriadPro, 'Myriad Pro', Calibri, sans-serif"
        :font-style  "normal"
        :font-weight "normal"))

(defn monospace []
  (list :font-family "'Droid Sans Mono', monospace, sans-serif"
        :font-style  "normal"
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
     ]

    [:body
     :margin 0
     :padding 0
     :height "100%"
     :color (hsl (:color *base*))
     (segoe-ui)
     (font-size)
     (font-weight)
     (line-height)
     ]

    ["html, body"
     :background-color :#E8E8E8
     (linear-gradient ["left top" :#8494ad :#d69baf])
     :background-attachment :fixed
     ]

    ["p, ul, ol, dl, pre"
     :margin-top    (px vr)
     :margin-bottom (px vr)
     ]

    ["code, pre"
     (monospace)
     ]

    ; Links
    [:a
     (transition [:all :0.2s :ease-out])

     ["&:link"
      :color :#CF2F0B
      :text-decoration :none]

     ["&:visited"
      :color :#CF2F0B
      :text-decoration :none]

     ["&:hover, &:focus"
      :color (rgb-hexstr (darken (create-color "#CF2F0B") 15))
      :text-decoration :none
      ]

     ["&:active"
      :color :#CF2F0B
      :text-decoration :none]]

    ; Headings
    ["h1, h2, h3, h4, h5, h6"
     (segoe-ui)
     :font-weight "normal"
     ]

    [:h1
     :display :inline-block
     :margin 0
     :color :#FBFBFB
     (font-size 2.4)
     ]

    [:h2
     ]

    ; Header
    ["header[role=banner]"
     :margin-top  :50px
     ]

    ; Paste
    [:section#paste
     :position :relative
     :margin-top    :50px
     :margin-bottom :50px
     :padding 0
     :background-color :#FBFBFB
     (box-shadow [0 0 :10px (hsla 0 0 0 0.25)])
     (box-sizing :border-box)
     ]

    [:form
     :margin-top (px vr)

     [:textarea
      :display :block
      :margin 0
      :padding (px vr)
      :padding-bottom 0
      :width "100%"
      :min-height (px (* 15 vr))
      :border :none
      (box-sizing :border-box)
      :background-color :transparent
      :resize :none
      :color :#585856
      (monospace)
      :-webkit-appearance :none

      ["&:focus"
       :outline :none]
      ]

     ["textarea + div > .syntaxhighlighter"
      :border-top [(px 1) :solid :#E9E9E9]
      ]

     ["input[type=submit], button"
      :-webkit-appearance :none
      ]
     ]

    ; Code
    [:div.code
     :display :block
     :margin 0
     :width "100%"
     :min-height (px (* 15 vr))
     (box-sizing :border-box)

     [:.syntaxhighlighter
      :padding [(px vr) 0]
      [:table
       :width "100%"
       [:.line
        :padding [0 (px vr)]]]]
     ]

    ; Output
    [:ul.output
     :margin 0
     :padding (px vr)
     :padding-top 0
     :border-top [(px 1) :solid :#E9E9E9]
     :list-style-type :none

     [:li
      :margin 0
      :padding 0

      ["&:last-child"
       [:pre
        :margin-bottom 0]]
      ]
     ]

    ; Actions
    [:ul.actions
     :position :absolute
     :top 0
     :right 0
     :margin 0
     :padding [0 (px vr)]
     :border-left [(px 1) :solid :#E9E9E9]
     :background-color :#FBFBFB
     :list-style-type :none

     [:li
      :display :inline

      ["a, input"
       :display :inline-block
       :padding [:20px (px vr)]
       :border 0
       :color :#585856
       (segoe-ui)
       (transition [:all :0.2s :ease-out])

       ["&:hover"
        :color :#CF2F0B]

       ["&:disabled"
        :color :#E8E8E8]
      ]]
     ]

    ; Meta
    [:p.meta
     :display :inline-block
     :position :absolute
     :bottom (px vr)
     :right 0
     :margin 0
     :padding (px vr)
     :padding-bottom 0
     :color :#C8C8C8]

    ; Fork Of
    [:#fork-of
     :padding (px vr)
     :border-top [(px 1) :solid :#E9E9E9]

     [:p
      :margin 0]]

    ; Footer
    [:footer
     :margin-top :50px
     :margin-bottom :50px
     :color (hsla 0 0 0 0.25)
     (font-size 1.3)]

    ; Muted
    [:.muted
     :color :#E8E8E8]

    ; Hidden
    [:.hidden
     :display "none !important"]

    ; Selection
    ["::-moz-selection"
     :background-color :#d6d6d6]
    ["::selection"
     :background-color :#d6d6d6]

    ))


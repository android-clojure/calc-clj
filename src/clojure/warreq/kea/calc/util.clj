(ns warreq.kea.calc.util
  (:require [neko.context :refer [get-service]]
            [neko.ui.mapping :refer [defelement]]
            [neko.ui.traits :refer [deftrait]]
            [neko.find-view :refer [find-view]])
  (:import android.graphics.Typeface
           android.view.inputmethod.EditorInfo
           android.graphics.Color
           me.grantland.widget.AutofitTextView))

(deftrait :min-text-size
  "Takes `:min-text-size` attribute which should be an integer in scaled pixels,
  and uses that to know the smallest size the widget may be transformed to."
  [^android.widget.TextView wdg, {:keys [min-text-size]} _]
  (.setMinTextSize wdg min-text-size))

(deftrait :max-text-size
  "Takes `:max-text-size` attribute which should be an integer in scaled pixels,
  and uses that to know the largest size the widget may be transformed to."
  [^android.widget.TextView wdg, {:keys [max-text-size]} _]
  (.setMaxTextSize wdg max-text-size))

(defelement :edit-text
  :classname android.widget.EditText
  :inherits :text-view
  :values {:number      EditorInfo/TYPE_CLASS_NUMBER
           :text        EditorInfo/TYPE_CLASS_TEXT})

(defelement :autofit-text
  :classname me.grantland.widget.AutofitTextView
  :inherits :text-view
  :traits [:min-text-size :max-text-size])

(defn vibrate!
  "Vibrate phone for n milliseconds. Convenience function for avoiding use of
  refection when acquiring the Vibrator Service via `neko.context`."
  [n]
  (let [vibrator (cast android.os.Vibrator (get-service :vibrator))]
    (.vibrate ^android.os.Vibrator vibrator n)))

(defn screen-height
  []
  (let [wm (cast android.view.WindowManager (get-service :window))
        display (.getDefaultDisplay wm)]
    (.getHeight display)))

(defn screen-width
  []
  (let [wm (cast android.view.WindowManager (get-service :window))
        display (.getDefaultDisplay wm)]
    (.getWidth display)))

(defn number-button
  "Build a button element for a number."
  [value handler]
  (let [x (if (number? value) (+ value 1) 1)
        d (* 60 (/ x 2))]
    [:button {:layout-width 0
              :layout-height :fill
              :layout-weight 1
              :background-color Color/TRANSPARENT
              :typeface Typeface/MONOSPACE
              :text (str value)
              :on-click (fn [_] (vibrate! d) (handler value))}]))

(defn op-button
  "Build a button element for an operator."
  [value handler cfg]
  [:button (merge {:layout-width 0
                   :layout-height :fill
                   :layout-weight 1
                   :text (str value)
                   :on-click (fn [_] (vibrate! 10) (handler value))} cfg)])

(defn display-element
  "Create a UI widget for displaying a right-justified text-field."
  [id cfg]
  [:linear-layout {:orientation :vertical
                   :layout-width :fill
                   :layout-height [(/ (screen-height) 15) :dip]}
   [:autofit-text (merge {:id id
                          :min-text-size 10
                          :max-text-size (/ (screen-height) 16)
                          :layout-height :wrap-content
                          :layout-width :fill
                          :single-line true
                          :max-lines 1
                          :typeface Typeface/MONOSPACE
                          :gravity :right}
                         cfg)]])

(defn display-element-landscape
  "Create a UI widget for displaying a right-justified text-field, in landscape."
  [id cfg]
  [:linear-layout {:orientation :vertical
                   :layout-width :fill
                   :layout-height [(/ (screen-height) 15) :dip]}
   [:autofit-text (merge {:id id
                          :min-text-size 10
                          :max-text-size (/ (screen-height) 16)
                          :layout-height :wrap-content
                          :layout-width :fill
                          :single-line true
                          :max-lines 1
                          :typeface Typeface/MONOSPACE
                          :gravity :right}
                         cfg)]])

(defn label-element
  "Create a UI widget for displaying a left-justified label."
  [id cfg]
  [:text-view (merge {:id id
                      :text-size [22 :sp]
                      :layout-height [60 :sp]
                      :gravity :left
                      :layout-width :wrap-content}
                     cfg)])

(def row-attributes
  {:orientation :horizontal
   :layout-width :fill
   :layout-height 0
   :layout-weight 1
   :background-color Color/TRANSPARENT})

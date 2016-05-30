(ns warreq.kea.calc.calc
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.ui :refer [config get-screen-orientation]]
            [neko.intent :refer [intent]]
            [neko.resource :as res]
            [neko.debug :refer [*a]]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [warreq.kea.calc.util :as u]
            [warreq.kea.calc.math :as math])
  (:import android.widget.EditText
           android.widget.TextView
           android.graphics.Typeface
           android.text.InputType))

;; We execute this function to import all subclasses of R class. This gives us
;; access to all application resources.
(res/import-all)

;; Calculator state =============================================================
(def expression (atom []))

(def stack (atom '()))

(defn ^android.widget.EditText input
  "Fetch the Input EditText field."
  []
  (find-view (*a) ::z))

(defn input-text
  "Fetch the current text from the Input EditText field"
  []
  (.toString (.getText (input))))

(defn toggle-edit-input
  "Enable numeric input to the Input widget via the device's keyboard."
  []
  (config (input) :input-type InputType/TYPE_CLASS_NUMBER))

(defn show-stack! []
  (let [a (*a)]
    (.startActivity ^android.app.Activity a (intent a '.StackView {}))))

;; Handler functions ============================================================
(defn return-handler
  [_]
  (when (> (count (input-text)) 0)
    (swap! stack conj (bigdec (read-string (input-text)))))
  (.setText (input) ""))

(defn num-handler
  [n]
  (.setText (input) (str (input-text) n)))

(defn op-handler
  [op]
  (when (> (count (input-text)) 0)
    (return-handler op))
  (when (>= (count (deref stack)) 2)
    (swap! expression conj (math/op-alias op))
    (when-let [result (math/rpn (apply list (deref expression)) (deref stack))]
      (reset! expression [result])
      (reset! stack (drop 2 (deref stack)))
      (swap! stack conj (first (deref expression)))
      (reset! expression []))))

(defn clear-handler
  [_]
  (.setText (input) "")
  (reset! expression [])
  (reset! stack '()))

(defn backspace-handler
  [_]
  (let [cur (input-text)]
    (when (> (count cur) 0)
      (.setText (input) (.substring ^String cur 0 (- (count cur) 1)))
      (when (= "-" (input-text))
        (.setText (input) "")))))

(defn invert-handler
  [_]
  (let [cur (input-text)]
    (when (> (count cur) 0)
      (if (= (.charAt ^String cur 0) \-)
        (.setText (input) (.substring ^String cur 1 (count cur)))
        (.setText (input) (str "-" cur))))))

;; Layout Definitions ===========================================================
(def op-column
  [(u/operator-button "÷" op-handler)
   (u/operator-button "×" op-handler)
   (u/operator-button "-" op-handler)])

(defn main-layout
  [landscape?]
  (let [disp (if landscape? u/display-element-landscape u/display-element)]
    (concat
     [:linear-layout {:orientation :vertical}
      (disp ::w {:on-long-click (fn [_] (show-stack!))})
      (disp ::x {:on-long-click (fn [_] (show-stack!))})
      (disp ::y {:on-long-click (fn [_] (show-stack!))})
      [:edit-text {:id ::z
                   :input-type 0
                   :single-line true
                   :layout-height (if landscape? [34 :dp] [64 :dp])
                   :text-size (if landscape? [22 :sp] [44 :sp])
                   :typeface android.graphics.Typeface/MONOSPACE
                   :gravity :left
                   :layout-width :fill
                   :on-long-click (fn [_] (toggle-edit-input))}]]
     [[:linear-layout u/row-attributes
       (u/operator-button "CLEAR" clear-handler)
       (u/operator-button "BACK" backspace-handler)
       (u/operator-button "±" invert-handler)
       (u/operator-button "^" op-handler)]]
     (map (fn [i]
            (concat
             [:linear-layout u/row-attributes]
             (map (fn [j]
                    (let [n (+ (* i 3) j)]
                      (u/number-button n num-handler)))
                  (range 1 4))
             [(get op-column i)]))
          (range 3))
     [[:linear-layout u/row-attributes
       (u/operator-button "RET" return-handler)
       (u/number-button 0 num-handler)
       (u/number-button "." num-handler)
       (u/operator-button "+" op-handler)]])))

;; Activities ===================================================================
(defactivity warreq.kea.calc.Calculator
  :key :main
  :features [:no-title]
  (onCreate [this bundle]
            (.superOnCreate this bundle)
            (neko.debug/keep-screen-on this)
            (let [landscape? (= (get-screen-orientation) :landscape)]
              (on-ui
               (set-content-view! (*a) (main-layout landscape?))))
            (let [^TextView z (find-view (*a) ::z)
                  ^TextView y (find-view (*a) ::y)
                  ^TextView x (find-view (*a) ::x)
                  ^TextView w (find-view (*a) ::w)]
              (add-watch stack :stack
                         (fn [key atom old new]
                           (.setText y (str (first new)))
                           (.setText x (str (second new)))
                           (.setText w (str (nth new 2 "")))))))
  (onResume [this]
            (.superOnResume this)
            ;; Force an event to make the watchers update
            (on-ui (reset! stack (deref stack)))))

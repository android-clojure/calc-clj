(ns warreq.kea.calc.main
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.notify :refer [toast]]
            [neko.intent :refer [intent]]
            [neko.resource :as res]
            [neko.debug :refer [*a]]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [warreq.kea.calc.util :as u]
            [warreq.kea.calc.calc :as calc])
  (:import android.widget.EditText))

;; We execute this function to import all subclasses of R class. This gives us
;; access to all application resources.
(res/import-all)

(defn show-stack! []
  (let [a (*a)]
    (.startActivity a (intent a '.StackView {}))))

'(defn notify-from-edit
   "Finds an EditText element with ID ::user-input in the given activity. Gets
  its contents and displays them in a toast if they aren't empty. We use
  resources declared in res/values/strings.xml."
   [activity]
   (let [^EditText input (.getText (find-view activity ::user-input))]
     (toast (if (empty? input)
              (res/get-string R$string/input_is_empty)
              (res/get-string R$string/your_input_fmt input))
            :long)))

(def op-column
  [(u/button-element "÷" calc/op-handler)
   (u/button-element "×" calc/op-handler)
   (u/button-element "-" calc/op-handler)])

(def main-layout
  (concat
   [:linear-layout {:orientation :vertical}
    (u/display-element ::w {:on-long-click (fn [_] (show-stack!))})
    (u/display-element ::x {:on-long-click (fn [_] (show-stack!))})
    (u/display-element ::y {:on-long-click (fn [_] (show-stack!))})
    (u/display-element ::z {})]
   [[:linear-layout u/row-attributes
     (u/button-element "CLEAR" calc/clear-handler)
     (u/button-element "BACK" calc/backspace-handler)
     (u/button-element "±" calc/invert-handler)
     (u/button-element "^" calc/op-handler)]]
   (map (fn [i]
          (concat
           [:linear-layout u/row-attributes]
           (map (fn [j]
                  (let [n (+ (* i 3) j)]
                    (u/button-element n calc/num-handler)))
                (range 1 4))
           [(get op-column i)]))
        (range 3))
   [[:linear-layout u/row-attributes
     (u/button-element "RET" calc/return-handler)
     (u/button-element 0 calc/num-handler)
     (u/button-element "." calc/num-handler)
     (u/button-element "+" calc/op-handler)]]))

(defactivity warreq.kea.calc.MainActivity
  :key :main
  :features [:no-title]
  (onCreate [this bundle]
            (.superOnCreate this bundle)
            (neko.debug/keep-screen-on this)
            (on-ui
             (set-content-view! (*a) main-layout))
            (let [^android.widget.TextView z (find-view (*a) ::z)
                  ^android.widget.TextView y (find-view (*a) ::y)
                  ^android.widget.TextView x (find-view (*a) ::x)
                  ^android.widget.TextView w (find-view (*a) ::w)]
              (add-watch calc/input :input
                         (fn [key atom old new]
                           (.setText z ^String new)))
              (add-watch calc/stack :stack
                         (fn [key atom old new]
                           (.setText y (str (first new)))
                           (.setText x (str (second new)))
                           (.setText w (str (nth new 2 ""))))))))

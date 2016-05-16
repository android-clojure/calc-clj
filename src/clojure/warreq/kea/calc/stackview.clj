(ns warreq.kea.calc.stackview
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.ui.adapters :refer [ref-adapter]]
            [neko.ui :refer [config]]
            [neko.find-view :refer [find-view]]
            [neko.debug :refer [*a]]
            [neko.threading :refer [on-ui]]
            [warreq.kea.calc.util :as u]
            [warreq.kea.calc.calc :as calc]))

(defn make-adapter []
  (ref-adapter
   (fn [_] [:linear-layout {:layout-width :fill :id-holder true}
            (u/label-element ::label {:id ::label})
            (u/display-element ::reg {:id ::reg})])
   (fn [position view _ data]
     (let [reg (find-view view ::reg)
           lbl (find-view view ::label)
           sym (if (< position 26) (char (+ 97 position)) position)]
       (config reg :text (str data))
       (config lbl :text (str " " sym "."))))
   calc/stack
   reverse))

(defactivity warreq.kea.calc.StackView
  :key :stack
  :features [:no-title]
  (onCreate [this bundle]
            (.superOnCreate this bundle)
            (neko.debug/keep-screen-on this)
            (on-ui
             (set-content-view! (*a) [:list-view
                                      {:adapter (make-adapter)}]))))



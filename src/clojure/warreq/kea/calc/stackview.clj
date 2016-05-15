(ns warreq.kea.calc.stackview
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.ui.adapters :refer [ref-adapter]]
            [neko.ui :refer [config]]
            [neko.find-view :refer [find-view]]
            [neko.debug :refer [*a]]
            [neko.threading :refer [on-ui]]
            [warreq.kea.calc.calc :as calc]))

(defn make-adapter []
  (ref-adapter
   (fn [_] [:linear-layout {:id-holder true}
            [:text-view {:id ::reg}]])
   (fn [position view _ data]
     (let [reg (find-view view ::reg)
           sym (if (< position 26) (char (- 122 position)) position)]
       (config reg :text (str sym ". " data))))
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



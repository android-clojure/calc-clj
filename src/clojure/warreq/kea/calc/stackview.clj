(ns warreq.kea.calc.stackview
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.resource :as res]
            [neko.debug :refer [*a]]
            [neko.threading :refer [on-ui]]
            [warreq.kea.calc.util :as u]
            [warreq.kea.calc.calc :as calc]))

(def stack-layout
  (concat
   [:linear-layout {:orientation :vertical}
    [:linear-layout u/row-attributes
     (u/button-element (first (deref calc/stack)) calc/clear-handler)]]
   [[:linear-layout u/row-attributes
     (u/button-element (second (deref calc/stack)) calc/clear-handler)]]))

(defactivity warreq.kea.calc.StackView
  :key :stack
  :features [:no-title]
  (onCreate [this bundle]
            (.superOnCreate this bundle)
            (neko.debug/keep-screen-on this)
            (on-ui
             (set-content-view! (*a) stack-layout))))



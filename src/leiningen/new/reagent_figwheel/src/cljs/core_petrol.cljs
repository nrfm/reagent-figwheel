(ns {{ns-name}}.core
  (:require
   [reagent.core :as reagent]
   [petrol.core :as petrol]{{#devtools?}}
   [devtools.core :as devtools]{{/devtools?}}
   ))


(defonce debug?
  ^boolean js/goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode"){{#devtools?}}
    (devtools/install!){{/devtools?}}
    ))


;; Model

(def initial-state
  {:counter 0})

(defonce app-state
  (reagent/atom initial-state))


;; Update

(defrecord Decrement []
  petrol/Message
  (process-message [_ app]
    (update app :counter dec)))

(defrecord Increment []
  petrol/Message
  (process-message [_ app]
    (update app :counter inc)))


;; View

(defn page [ui-channel app]
  [:div
   [:button {:on-click (petrol/send! ui-channel (->Decrement))}
    "Decrement"]
   [:button {:on-click (petrol/send! ui-channel (->Increment))}
    "Increment"]
   [:p "Count: " (:counter app)]])


;; Initialize App

(defn reload []
  (swap! app-state identity))

(defn render-fn [ui-channel app]
  (reagent/render [page ui-channel app]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (petrol/start-message-loop! app-state render-fn))

(ns socializer.core
  (:gen-class)
  (:require [socializer.web :as web]
            [org.httpkit.server :as httpkit]))

(defn -main
  [& args]
  (println "Hello, World!")
  (httpkit/run-server web/handler {:port 8000}))

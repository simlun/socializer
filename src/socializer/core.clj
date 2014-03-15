(ns socializer.core
  (:gen-class)
  (:require [socializer.web :as web]
            [org.httpkit.server :as httpkit]))

(defn -main
  [& args]
  (let [port (Integer. (first args))]
    (println "Starting Socializer web server on port" port "...")
    (httpkit/run-server web/handler {:port port})))

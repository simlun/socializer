(ns socializer.core
  (:gen-class)
  (:require [socializer.web.routes :as routes]
            [org.httpkit.server :as httpkit]))

(defn -main
  [& args]
  (let [port (Integer. (first args))]
    (println "Starting Socializer web server on port" port "...")
    (httpkit/run-server routes/handler {:port port})))

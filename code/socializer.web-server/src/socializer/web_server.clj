(ns socializer.web-server
  (:use org.httpkit.server)
  (:gen-class))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/plain"}
   :body    "OK"})

(defn -main
  [& args]
  (println "Starting web server...")
  (run-server app {:port 8000}))

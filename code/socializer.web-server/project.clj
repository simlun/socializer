(defproject socializer.web-server "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.1.16"]]
  :main ^:skip-aot socializer.web-server
  :profiles {:uberjar {:aot :all}})

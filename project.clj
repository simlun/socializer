(defproject socializer "0.1.0-SNAPSHOT"
  :description "Heuristic for socializing efficiently"
  :url "http://github.com/simlun/socializer"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [javax.servlet/servlet-api "2.5"]
                 [http-kit "2.1.16"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler socializer.web/handler}
  :main ^:skip-aot socializer.core
  :uberjar-name "socializer-standalone.jar"
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

(defproject socializer "0.1.0-SNAPSHOT"
  :description "Heuristic for socializing efficiently"
  :url "http://github.com/simlun/socializer"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :main ^:skip-aot socializer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

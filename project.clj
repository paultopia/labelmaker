(defproject labelmaker "0.1.0-SNAPSHOT"

  :description "Data entry application for documents."
  :url "https://github.com/paultopia/labelmaker"

  :dependencies [[buddy "1.3.0"]
                 [cider/cider-nrepl "0.15.0-SNAPSHOT"]
                 [clj-time "0.14.0"]
                 [cljs-ajax "0.6.0"]
                 [compojure "1.6.0"]
                 [conman "0.6.7"]
                 [cprop "0.1.10"]
                 [funcool/struct "1.0.0"]
                 [luminus-immutant "0.2.3"]
                 [luminus-migrations "0.3.9"]
                 [luminus-nrepl "0.1.4"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "0.9.99"]
                 [metosin/muuntaja "0.3.1"]
                 [metosin/ring-http-response "0.9.0"]
                 [mount "0.1.11"]
                 [org.clojure/clojure "1.9.0-alpha17"]
                 [org.clojure/clojurescript "1.9.671" :scope "provided"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.postgresql/postgresql "42.1.1"]
                 [org.webjars.bower/tether "1.4.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.5"]
                 [org.webjars/font-awesome "4.7.0"]
                 [reagent "0.7.0"]
                 [reagent-utils "0.2.1"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.6.2"]
                 [ring/ring-defaults "0.3.0"]
                 [secretary "1.2.3"]
                 [selmer "1.10.9"]
                 [com.novemberain/langohr "3.6.1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.3.443"]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main ^:skip-aot labelmaker.core
  :migratus {:store :database :db ~(get (System/getenv) "DATABASE_URL")}

  :plugins [[lein-cprop "1.0.3"]
            [migratus-lein "0.4.9"]
            [lein-cljsbuild "1.1.5"]
            [lein-immutant "2.1.0"]]
  :clean-targets ^{:protect false}
  [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]
  :figwheel
  {:http-server-root "public"
   :nrepl-port 7002
   :css-dirs ["resources/public/css"]
   :nrepl-middleware
   [cemerick.piggieback/wrap-cljs-repl cider.nrepl/cider-middleware]}
  

  :profiles
  {:uberjar {:omit-source true
             :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
             :cljsbuild
             {:builds
              {:min
               {:source-paths ["src/cljc" "src/cljs" "env/prod/cljs"]
                :compiler
                {:output-to "target/cljsbuild/public/js/app.js"
                 :optimizations :advanced
                 :pretty-print false
                 :closure-warnings
                 {:externs-validation :off :non-standard-jsdoc :off}
                 :externs ["react/externs/react.js"]}}}}
             
             
             :aot :all
             :uberjar-name "labelmaker.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:dependencies [[prone "1.1.4"]
                                 [ring/ring-mock "0.3.1"]
                                 [ring/ring-devel "1.6.2"]
                                 [pjstadig/humane-test-output "0.8.2"]
                                 [binaryage/devtools "0.9.4"]
                                 [com.cemerick/piggieback "0.2.2"]
                                 [doo "0.1.7"]
                                 [figwheel-sidecar "0.5.11"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.19.0"]
                                 [lein-doo "0.1.7"]
                                 [lein-figwheel "0.5.11"]
                                 [org.clojure/clojurescript "1.9.671"]]
                  :cljsbuild
                  {:builds
                   {:app
                    {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                     :figwheel {:on-jsload "labelmaker.core/mount-components"}
                     :compiler
                     {:main "labelmaker.app"
                      :asset-path "/js/out"
                      :output-to "target/cljsbuild/public/js/app.js"
                      :output-dir "target/cljsbuild/public/js/out"
                      :source-map true
                      :optimizations :none
                      :pretty-print true}}}}
                  
                  
                  
                  :doo {:build "test"}
                  :source-paths ["env/dev/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:resource-paths ["env/test/resources"]
                  :cljsbuild
                  {:builds
                   {:test
                    {:source-paths ["src/cljc" "src/cljs" "test/cljs"]
                     :compiler
                     {:output-to "target/test.js"
                      :main "labelmaker.doo-runner"
                      :optimizations :whitespace
                      :pretty-print true}}}}
                  
                  }
   :profiles/dev {}
   :profiles/test {}})

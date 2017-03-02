(defproject hn-scrapper "0.1.2"
  :description "Easy ways to access all of the latest Hacker News links in one page"
  :url "https://github.com/agilecreativity/hn-scrapper"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[lein-bin "0.3.5"]]}
             :uberjar {:aot :all}}
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :bin {:name "hn-scrapper"
        :bin-path "~/bin"
        :bootclasspath true}
  :plugins [[lein-bin "0.3.5"]
            [lein-cljfmt "0.5.6"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [reaver "0.1.2"]
                 [org.clojure/tools.cli "0.3.5"]
                 [me.raynes/fs "1.4.6"]]
  :main com.agilecreativity.hn_scrapper.main)

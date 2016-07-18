(ns com.agilecreativity.hn_scrapper.main
  (:require [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [clojure.tools.cli :refer [parse-opts] :as cli]
            [com.agilecreativity.hn_scrapper.option :refer :all :as opt]
            [me.raynes.fs :as fs]
            [reaver :refer [parse extract-from text attr]])
  (:gen-class))

(defn- hacker-news-url
  "Return URL from hacker-news for a given page"
  [page-number]
  (str "https://news.ycombinator.com/news?p=" page-number))

(defn- hacker-news
  "Read the content of hacker-news for a given page"
  [page-number]
  (slurp (hacker-news-url page-number)))

(defn extract-data
  "Extract some data from hacker-news"
  [page-number]
  (extract-from (parse (hacker-news page-number)) ".itemlist .athing"
                [:headline :url]
                ".title > a" text
                ".title > a" (attr :href)))

(defn markdown-links
  "Create basic markdown link from content map"
  [content]
  (map #(str "[" (:headline %) "](" (:url %) ")") content))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]}
        (cli/parse-opts args opt/options)]
    (cond
      (:help options)
      (exit 0 (usage summary)))
    ;; If we get here, then we have all the argument that we need
    (let [output-file (-> (:output-file options)
                          (fs/expand-home)
                          (fs/normalized))
          page-count (:page-count options)]
      (do
        (println "Number of pages to be selected : " page-count)
        (println "Output file : " output-file)
        (with-open [w (io/writer output-file)]
          (.write w (str "### The latest " page-count " pages from Hacker News"))
          (.newLine w)
          ;; Note: Hacker News only show the last 20 pages
          (doseq [n (range (Integer. page-count))]
            ;(println "Processing page " (inc n))
            (let [content (extract-data (inc n))]
              ;; Let sleep 1 second between new request to be polite
              (Thread/sleep 1000)
              ;; And get just the field we need
              (doseq [line (markdown-links content)]
                ;; Print out so we know where we are (optional)
                (pp/pprint line)
                ;; Print this as a list
                (.write w (str " - " line))
                (.newLine w)))))))))

(ns basic-http-server.movies-by-ship.core
  (:require
    [ring.util.http-response :refer :all]
    [clj-http.client :as client]
    [cheshire.core :refer :all]
    [clojure.set :refer :all]
    [basic-http-server.movies-by-ship.db :as db]
    [hikari-cp.core :as hikari]
    [next.jdbc :as jdbc]
    ))


(defn get-data [request]
  (let [response (client/get request {:insecure? true})
        body (:body response)
        data (parse-string body true)]
    data))

(defn get-swapi
  ([request] (get-data request))
  ([path params] (let [
                       server (str "https://swapi.dev/api/")
                       request (str server path params)
                       data (get-data request)
                       ]
                   data))
  )

(def path "/starships/?search=")
(defn fetch-ship-by-name [name]
  (get-swapi path name)
  )

(defn fetch-pilot [pilot-request]
  (get-swapi pilot-request)
  )

(defn fetch-film [film-request]
  (get-swapi film-request)
  )


(def db-pool
  (hikari/make-datasource
    {:jdbc-url     "jdbc:postgresql://localhost:5432/thedbitself"
     :username     "thedbuser"
     :password     "thedbpassword"
     :maximum-pool-size 10}))

;; Create a connection
(def ds (jdbc/get-datasource db-pool))


(defn fetch-movies-service [name]
  (let [
        ships (fetch-ship-by-name name)
        ship-pilots (map (fn [s] (:pilots s)) (:results ships))
        pilots (if (empty? ship-pilots) () (distinct (reduce union ship-pilots)))
        pilots-info (map (fn [p] (fetch-pilot p)) pilots)
        pilots-films (map (fn [p] (:films p)) pilots-info)
        films (if (empty? pilots-films) () (distinct (reduce union pilots-films)))
        films-info (map (fn [p] (fetch-film p)) films)
        title  (map (fn [p] (:title p)) films-info)]
     title
    )
  )

(defn fetch-movies-db [name]
  (let
    [db-result (db/get-movies ds {:ship_name name})
     movies (map (fn [m] (:movie_name m)) db-result)
     ]
    movies
    )
  )

(defn save-in-db [ship-name movies]
  (cond
    (not-empty movies) (map (fn [m] (db/create-movies! ds {:ship_name ship-name :movie_name m})) movies)
    :else   (db/create-movies! ds {:ship_name ship-name :movie_name ""} movies)
    )
  ;; TODO refactor to save in BD in one call
  )

; (reduce union pilots)
; data (movies-by-ship-name {:ship_name "Star", :cols ["ship_name" "movie_name"]})
(defn handle-movies [{{{:keys [name]} :query} :parameters}]
  (let [movies-from-db (fetch-movies-db name)
        not-in-db? (empty? movies-from-db)
        movies (if not-in-db? (fetch-movies-service name) movies-from-db)
        ]
    (do
      (if not-in-db? (save-in-db name movies) ) ;; TODO make it parallel
      {:status 200
       :body movies
       }
      )
    )
  )

(comment
  ;; (distinct (union (map (fn [el] el), pilots)))
  (let [
        name (str "Start Destroyer")
        data (:body (client/get (str server path name) {:insecure? true})  )
        raw (parse-string data true)
        ;; now I have data
        ]
    {:status 200
     :body raw
     }
    )

  )
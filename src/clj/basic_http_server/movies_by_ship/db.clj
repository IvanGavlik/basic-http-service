(ns basic-http-server.movies-by-ship.db
  (:require
    [hugsql.core :as hugsql]
    )
  )

(hugsql/def-db-fns "sql/movies.sql")


(hugsql/def-sqlvec-fns "sql/movies.sql")
CREATE DATABASE flights;

CREATE TABLE IF NOT EXISTS flights.airports (
  airport_id INT,
  city STRING,
  state STRING,
  name STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
COLLECTION ITEMS TERMINATED BY '\n'
STORED AS TEXTFILE
tblproperties ("skip.header.line.count"="1");

LOAD DATA INPATH '/data/airports.csv' OVERWRITE INTO TABLE flights.airports;

CREATE TABLE IF NOT EXISTS flights.flights (
  DayofMonth INT,
  DayOfWeek INT,
  Carrier STRING,
  OriginAirportID INT,
  DestAirportID INT,
  DepDelay INT,
  ArrDelay INT
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
COLLECTION ITEMS TERMINATED BY '\n'
STORED AS TEXTFILE
tblproperties ("skip.header.line.count"="1");

LOAD DATA INPATH '/data/flights.csv' OVERWRITE INTO TABLE flights.flights;

-- Загруженность аэропортов по количеству вылетов в день месяца и день недели
CREATE TABLE flights.departures_day AS
SELECT DISTINCT
    dayofmonth,
    dayofweek,
    city,
    state,
    name,
    COUNT(originairportid) OVER (PARTITION BY originairportid, dayofmonth) departures_by_day_of_month,
    COUNT(originairportid) OVER (PARTITION BY originairportid, dayofweek) departures_by_day_of_week
FROM flights.flights f
JOIN flights.airports a ON a.airport_id = f.originairportid
ORDER BY departures_by_day_of_month DESC, departures_by_day_of_week DESC;

-- Средняя задержка вылетов и прилетов в день месяца и день недели
CREATE TABLE flights.delay_day AS
SELECT DISTINCT
    dayofmonth,
    dayofweek,
    city,
    state,
    name,
    AVG(depdelay) OVER (PARTITION BY originairportid, dayofmonth) avg_depdelay_per_day_of_month,
    AVG(depdelay) OVER (PARTITION BY originairportid, dayofweek) avg_depdelay_per_day_of_week,
    AVG(arrdelay) OVER (PARTITION BY originairportid, dayofmonth) avg_arrdelay_per_day_of_month,
    AVG(arrdelay) OVER (PARTITION BY originairportid, dayofweek) avg_arrdelay_per_day_of_week
FROM flights.flights f
JOIN flights.airports a ON a.airport_id = f.originairportid
ORDER BY 
    avg_depdelay_per_day_of_month DESC,
    avg_depdelay_per_day_of_week DESC,
    avg_arrdelay_per_day_of_month DESC,
    avg_arrdelay_per_day_of_week DESC
;

-- 10 самых популярных аэропортов назначения
CREATE TABLE flights.popular_airports AS
SELECT
    city,
    state,
    name,
    COUNT(destairportid) arr_count
FROM flights.flights f
JOIN flights.airports a ON a.airport_id = f.destairportid
GROUP BY destairportid, city, state, name
ORDER BY arr_count DESC
LIMIT 10;

-- 10 самых долгих средних задержек по аэропортам и перевозчикам
CREATE TABLE flights.top_delay AS
SELECT
    city,
    state,
    name,
    carrier,
    AVG(depdelay) avg_depdelay,
    AVG(arrdelay) avg_arrdelay
FROM flights.flights f
JOIN flights.airports a ON a.airport_id = f.originairportid
GROUP BY carrier, city, state, name
ORDER BY avg_depdelay DESC, avg_arrdelay DESC
LIMIT 10;


-- Количества аэропортов в штатах и городах
CREATE TABLE flights.airport_count AS
SELECT DISTINCT
    state,
    city,
    COUNT(name) OVER (PARTITION BY state) airports_in_state,
    COUNT(name) OVER (PARTITION BY city) airports_in_city
FROM flights.airports
ORDER BY airports_in_state DESC, airports_in_city DESC;

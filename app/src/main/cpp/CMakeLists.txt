cmake_minimum_required(VERSION 3.22.1)

project("travelplanner")

add_library(
        travelplanner
        SHARED
        native-lib.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        travelplanner
        ${log-lib})

package com.peatroxd.congratsinator.controller;

public class ApiPathsTestData {
    public static final String BIRTHDAY_CONTROLLER_DAYS_PARAM = "days";

    public static final String BIRTHDAY_CONTROLLER_BASE = "/api/birthdays";
    public static final String BIRTHDAY_CONTROLLER_GET_TODAY_BIRTHDAYS = BIRTHDAY_CONTROLLER_BASE + "/today";
    public static final String BIRTHDAY_CONTROLLER_GET_UPCOMING_BIRTHDAYS = BIRTHDAY_CONTROLLER_BASE + "/upcoming";

    public static final String PERSON_CONTROLLER_BASE = "/api/persons";
    public static final String PERSON_CONTROLLER_BY_ID = PERSON_CONTROLLER_BASE + "/{id}";

    public static final String PERSON_PHOTO_ENDPOINT = PERSON_CONTROLLER_BY_ID + "/photo";
}
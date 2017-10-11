package ru.javawebinar.topjava;

import javax.validation.groups.Default;

public class View {

    // https://narmo7.wordpress.com/2014/04/26/how-to-set-up-validation-group-in-springmvc/
    // http://beanvalidation.org/proposals/BVAL-234/
    public interface Persist extends Default {}
}
var folder = "./src/main/resources/static/";

var gulp = require('gulp'),
    uglify = require("gulp-uglify");

// task
gulp.task('minify-js', function () {
    gulp.src(folder + '*.js') // path to your files
    .pipe(uglify())
    .pipe(gulp.dest(folder + "min"));
});
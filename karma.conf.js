module.exports = function(config) {
  config.set({
    basePath: 'build/test/',
    browsers: ['ChromeHeadless'],

    frameworks: ['cljs-test'],
    reporters: ['progress', 'coverage', 'remap-coverage', 'coveralls'],
    preprocessors: {
      'darkleaf/form/**.js': ['coverage']
    },

    files: [
      'main.js',
      {pattern: '**/*.js', included: false, served: true},
      {pattern: '**/*.map', included: false, served: true}
    ],

    client: {
      // main function
      args: ['darkleaf.form_test.runner.run']
    },

    // singleRun set to false does not work!
    singleRun: true,

    coverageReporter: { type: 'in-memory' },
    remapCoverageReporter: {
      lcovonly: 'coverage/lcov.info'
    }
  })
}

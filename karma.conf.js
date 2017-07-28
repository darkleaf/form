module.exports = function(config) {
  config.set({
    basePath: 'build/test/',
    browsers: ['ChromiumHeadless'],

    frameworks: ['cljs-test'],
    reporters: ['progress', 'coverage', 'remap-coverage'],
    preprocessors: {
      'darkleaf/**/!(*_test).js': ['coverage']
    },

    files: [
      'main.js',
      {pattern: '**/*.js', included: false, served: true},
      {pattern: '**/*.map', included: false, served: true}
    ],

    client: {
      // main function
      args: ['darkleaf.form.test_runner.run']
    },

    // singleRun set to false does not work!
    singleRun: true,

    coverageReporter: { type: 'in-memory' },
    remapCoverageReporter: { html: './coverage' }
  })
}

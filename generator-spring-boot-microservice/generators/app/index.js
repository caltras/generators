'use strict';
const Generator = require('yeoman-generator');
const chalk = require('chalk');
const yosay = require('yosay');
const _ = require('lodash');
var rename = require("gulp-rename");

module.exports = class extends Generator {
  prompting() {
    this.log(
      yosay(
        `Welcome to the unreal ${chalk.red('generator-spring-boot-microservice')} generator!`
      )
    );

    const prompts = [
      {
        type: 'input',
        name: 'application.path',
        message: 'Define the project folder:',
        default: '/tmp/'
      },
      {
        type: 'input',
        name: 'application.name',
        message: 'What is the name of the project?',
        default: 'spring-example'
      },
      {
        type: 'input',
        name: 'application.group',
        message: 'What is the GROUP name of the project?',
        default: 'com.example'
      },
      {
        type: 'input',
        name: 'application.package',
        message: 'Define the package for the java project:',
        default: 'com.example.microservice.app'
      },
      {
        type: 'confirm',
        name: 'sonarqube.enabled',
        message: 'Would you like to enable sonarqube?',
        default: true
      }
    ];

    const promptsSonarQube = [
      {
        type: 'input',
        name: 'sonarqube.host.url',
        message: 'What is the Sonar endpoint?',
        default: 'https://sonarcloud.io/'
      },
      {
        type: 'input',
        name: 'sonarqube.login',
        message: 'What is sonar login?'
      },
      {
        type: 'input',
        name: 'sonarqube.projectKey',
        message: 'What is sonar project key?'
      },
    ];

    return this.prompt(prompts).then(props => {
      this.props = props;

      this.props.nameUpperCamelCase = _.camelCase(this.props.application.name);
      
      this.props.nameUpperCamelCase = this.props.nameUpperCamelCase[0].toUpperCase() 
        + this.props.nameUpperCamelCase.substring(1);

      if (this.props.sonarqube.enabled) {
        return this.prompt(promptsSonarQube).then( propsSonar => {
          this.props.sonarqube = Object.assign({}, this.props.sonarqube, {enabled: true}, propsSonar.sonarqube);
        })
      }

    });
  }

  writing() {
    const self = this;
    let packageName = self.props.application.package.replace(/\./g,'/');

    this.fs.copy(
      this.templatePath('gradle_resources'),
      this.destinationPath(this.props.application.path + "/" + this.props.application.name +  '/')
    );

    this.fs.copyTpl(
      this.templatePath('build/build.gradle'),
      this.destinationPath(this.props.application.path + "/" + this.props.application.name +  '/build.gradle'),
      this.props
    );

    self.registerTransformStream(rename((renamePath) => {

      let filePath= renamePath.basename;

      if (/\$.*\$/.test(filePath) > -1) {
        let matches = filePath.match(/\$.*\$/);
        if (matches){
          matches = matches.map( (p) => p.replace(/\$/g,'') );
          matches.forEach ((p) => {
            if (self.props.hasOwnProperty(p) && self.props[p]) {
              filePath = filePath.replace(new RegExp(p,'g'), self.props[p])
              filePath = filePath.replace(/\$/g,'');
            }
          });
        }
        
      }
      renamePath.basename = filePath;

    }));

    this.fs.copyTpl(
      this.templatePath('code/main/resources'),
      this.destinationPath(this.props.application.path + "/" + this.props.application.name + '/src/main/resources/'),
      this.props
    );

    this.fs.copyTpl(
      this.templatePath('code/main/java'),
      this.destinationPath(this.props.application.path + "/" + this.props.application.name + '/src/main/java/' + packageName),
      this.props
    );

    this.fs.copyTpl(
      this.templatePath('code/test/resources'),
      this.destinationPath(this.props.application.path + "/" + this.props.application.name + '/src/test/resources/'),
      this.props
    );
    this.fs.copyTpl(
      this.templatePath('code/test/functionaltest'),
      this.destinationPath(this.props.application.path + "/" + this.props.application.name + '/src/test/java/functionaltest/' + packageName),
      this.props
    );
  }

  install() {
    
  }
};

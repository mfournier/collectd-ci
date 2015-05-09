def githubOrg = 'collectd'

job('make-pr-tarball') {
  displayName('prepare tarball for pull-request testing')
  description("""
This job:
 * merges the pull-request with the master branch
 * generates a release tarball and archives it
 * exports a couple of environment variables to allow downstream tasks to refer to the release tarball

Configuration generated automatically, do not edit!
""")
  label('master')

  scm {
    git {
      remote {
        name('origin')
        url("https://github.com/${githubOrg}/collectd.git")
        refspec('+refs/pull/*/head:refs/remotes/origin/pr/*')
      }
      branch('${BUILD_GIT_COMMIT}')
      mergeOptions('origin', 'master')
    }
  }

  steps {
    shell('''\
./clean.sh
./build.sh
./configure
make dist-gzip

COLLECTD_BUILD="$(./version-gen.sh)"
TARBALL="collectd-$COLLECTD_BUILD.tar.gz"
PULL_REQUEST="$(basename $BUILD_GIT_BRANCH)"
test -f "$TARBALL"
test -n "$BUILD_NUMBER"
test -n "$PULL_REQUEST"

cat << EOF > "env-${BUILD_GIT_COMMIT}.sh"
COLLECTD_BUILD=$COLLECTD_BUILD
TARBALL=$TARBALL
TARBALL_BUILD_NUMBER=$BUILD_NUMBER
PULL_REQUEST=$PULL_REQUEST
EOF
''')
  }

  publishers {
    archiveArtifacts {
      pattern('collectd*.tar.gz')
    }
  }
}

multiJob('test-pull-requests') {
  displayName('test github pull-requests on various environements')
  description("""
This multi-step job aggregates various independent tasks, allowing to compute a global build status from them and report this back to github.

This job gets triggered by github when a pull-request is submitted/updated.

A couple of non-critical jobs are also defined as 'downstream jobs'. They are triggered only if all the others are successful and their exit status won't influence the global status.

Configuration generated automatically, do not edit!
""")
  label('master')

  concurrentBuild(false)

  scm {
    git {
      remote {
        name('origin')
        url("https://github.com/${githubOrg}/collectd.git")
        refspec('+refs/pull/*/head:refs/remotes/origin/pr/*')
        github("${githubOrg}/collectd")
        branch('origin/pr/*')
      }
      mergeOptions('origin', 'master')
    }
  }

  triggers {
    githubPush()
  }

  wrappers {
    environmentVariables {
      envs([
        BUILD_GIT_BRANCH: '$GIT_BRANCH',
        BUILD_GIT_COMMIT: '$GIT_COMMIT',
      ])
    }
  }

  downstreamProperties = [
    COLLECTD_BUILD:       '$COLLECTD_BUILD',
    TARBALL:              '$TARBALL',
    TARBALL_BUILD_NUMBER: '$TARBALL_BUILD_NUMBER',
    PULL_REQUEST:         '$PULL_REQUEST',
  ]

  configure { project ->
    project / builders / 'com.cloudbees.jenkins.GitHubSetCommitStatusBuilder'
  }

  steps {
    phase('prepare release tarball', 'SUCCESSFUL') {
      job('make-pr-tarball') {
        props([
          BUILD_GIT_BRANCH: '$BUILD_GIT_BRANCH',
          BUILD_GIT_COMMIT: '$BUILD_GIT_COMMIT',
        ])
      }
    }

    environmentVariables {
      propertiesFile('/var/lib/jenkins/jobs/make-pr-tarball/workspace/env-${BUILD_GIT_COMMIT}.sh')
    }

    // NB: unforunately "phase" blocks don't support groovy iterators, so this
    // forces us to file all the jobs manually here.
    phase('touchstone (won\'t continue further down if this step fails)', 'SUCCESSFUL') {
      job('build-on-jessie-amd64-with-default-toolchain') {
        props(downstreamProperties)
      }
    }

    phase('mandatory (platforms for which packages are built)', 'SUCCESSFUL') {
      job('build-on-jessie-i386-with-default-toolchain') {
        props(downstreamProperties)
      }
      job('build-on-trusty-amd64-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-trusty-i386-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-precise-amd64-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-precise-i386-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-wheezy-amd64-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-wheezy-i386-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-squeeze-amd64-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-squeeze-i386-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-epel7-x86_64-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-epel6-x86_64-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-epel6-i386-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-epel5-x86_64-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
      job('build-on-epel5-i386-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
    }

    phase('supported (platforms known to work that new patches shouldn\'t break)', 'SUCCESSFUL') {
      job('build-on-freebsd10-amd64-with-default-toolchain') {
        killPhaseCondition('NEVER')
        props(downstreamProperties)
      }
    }
  }

  publishers {
    githubCommitNotifier()
    downstreamParameterized {
      trigger('build-on-jessie-amd64-with-clang') {
        predefinedProps(downstreamProperties)
      }
      trigger('build-on-jessie-i386-with-clang') {
        predefinedProps(downstreamProperties)
      }
      trigger('build-on-jessie-amd64-with-clang-strict') {
        predefinedProps(downstreamProperties)
      }
      trigger('build-on-jessie-amd64-with-scan-build') {
        predefinedProps(downstreamProperties)
      }
    }
  }
}

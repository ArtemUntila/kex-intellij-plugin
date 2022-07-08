# kex-plugin

## Requirements

Specify local path to [kex](https://github.com/vorpal-research/kex) project folder: property `KEX_PATH` in [kexUtils.kt](src/main/kotlin/org/vorpal/research/kex/plugin/util/kexUtils.kt)

## Run

If Configuration `Run Plugin` is not set in IDE by default go to: `Gradle`(right toolbar): `kex-plugin/Tasks/intlellij/runIde`.
Run configuration.

In opened IDE, in Project View (on the left, where project file tree is displayed) select `.java`/`.kt` file, for which you want to generate tests with `kex`. 
Right-click will open context menu. Go to `Kex/Generate Tests`. Select folder, where you want to store generated tests. Enjoy.
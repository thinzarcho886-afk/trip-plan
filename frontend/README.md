# Project Setup and documentation

This template should help get you started developing with Vue 3 and TypeScript in Vite. The template uses Vue 3 `<script setup>` SFCs, check out the [script setup docs](https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup) to learn more.

## Table of Contents

1. [Prerequisite](#prerequisite)
1. [Recommended IDE Setup](#recommended-ide-setup)
1. [Type Support For `.vue` Imports in TS](#type-support-for-vue-imports-in-ts)
1. [Yarn, environments and scripits](#yarn-environments-and-scripits)
1. [Project Setup](#project-setup)
1. [Project Structure and Components](#project-structure-and-components)

## Prerequisite

_The package manager in use for this project is `Yarn`._

To install Yarn

```
npm install -g yarn
```

## Recommended IDE Setup

- [VS Code](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur) + [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin).

## Type Support For `.vue` Imports in TS

TypeScript cannot handle type information for `.vue` imports by default, so we replace the `tsc` CLI with `vue-tsc` for type checking. In editors, we need [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin) to make the TypeScript language service aware of `.vue` types.

If the standalone TypeScript plugin doesn't feel fast enough to you, Volar has also implemented a [Take Over Mode](https://github.com/johnsoncodehk/volar/discussions/471#discussioncomment-1361669) that is more performant. You can enable it by the following steps:

1. Disable the built-in TypeScript Extension
   1. Run `Extensions: Show Built-in Extensions` from VSCode's command palette
   2. Find `TypeScript and JavaScript Language Features`, right click and select `Disable (Workspace)`
2. Reload the VSCode window by running `Developer: Reload Window` from the command palette.

## Yarn, environments and scripits

### Environment Variables

The following environment variables are used in application:

**NODE_ENV** is used to specify the current environment.

**VITE_APP_BASE_PATH** is used to specify the base URL for application and is used by the vue-router library to generate URLs. For example, if application is hosted at https://example.com/my-app/, it would be set to /my-app/.

**VITE_APP_API_URL** is used to specify the base URL for API. For example, if your API is hosted at https://example.com/my-api/, it would be set to https://example.com/my-api/.

**VITE_APP_LOCALSTORAGE_PREFIX** is used to specify a prefix for keys that are stored in local storage.

### Scripts

These scripts are used to build and test your application in different environments.

**dev**

This script runs the development server using the vite command. It is used for local development. This will use `.env` file.

**livetest**

This script builds your application in staging mode using the vite build command. It is used to test your application in a staging environment. This will use `.env.staging` file.

**demo**

This script builds your application in testing mode using the vite build command. It is used to test your application in a testing environment. This will use `.env.testing` file.

**prod**

This script builds your application in production mode using the vite build command. It is used to build your application for deployment. This will use `.env.production` file.

## Project Setup

Install dependencies

```
yarn install
```

Run in development with hot-reload

### Compiles and hot-reloads for development

```
yarn dev
```

### Compiles and minifies for demo/uat testing

```
yarn demo
```

Note: change .env.testing if need

### Compiles and minifies for livetest

```
yarn livetest
```

Note: change .env.staging if need

### Compiles and minifies for production

```
yarn prod
```

## Project Structure and Components

Go to [PROJECT COMPONENTS](./PROJECT_COMPONENTS.md)

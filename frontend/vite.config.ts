import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import vuetify, { transformAssetUrls } from 'vite-plugin-vuetify';
import { resolve, dirname } from 'node:path';
import { fileURLToPath } from 'url';
import VueI18nPlugin from '@intlify/unplugin-vue-i18n/vite';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // https://vitejs.dev/config/#using-environment-variables-in-config

  // Load env file based on `mode` in the current working directory.
  // Set the third parameter to '' to load all env regardless of the `VITE_` prefix.
  const env = loadEnv(mode, process.cwd(), '');

  return {
    build: {
      // https://vitejs.dev/config/build-options.html#build-outdir
      outDir: '../backend/src/main/webapp/',
      // https://vitejs.dev/config/build-options.html#build-emptyoutdir
      emptyOutDir: true, // removing all files inside `webapp/`
    },
    base: env.VITE_APP_BASE_PATH,
    plugins: [
      vue({
        template: {
          transformAssetUrls, //Image loading
        },
      }),
      vuetify({ autoImport: true }), // Enabled by default
      VueI18nPlugin({
        /* options */
        // locale messages resource pre-compile option
        include: resolve(
          dirname(fileURLToPath(import.meta.url)),
          './src/locales/**'
        ),
        runtimeOnly: false,
      }),
    ],
    define: {
      'process.env.NODE_ENV':
        mode === 'production' ||
        mode === 'staging' ||
        mode === 'testing'
          ? '"production"'
          : '"development"',
    },
    css: {
      preprocessorOptions: {
        scss: {
          api: 'modern',
        },
      },
    },
    server: {
      port: 8082, // dev port
    },
  };
});

// Plugins
import vue from '@vitejs/plugin-vue'
import vuetify, { transformAssetUrls } from 'vite-plugin-vuetify'
import ViteFonts from 'unplugin-fonts/vite'
import { VitePWA } from 'vite-plugin-pwa'

// Utilities
import { defineConfig } from 'vite'
import { fileURLToPath, URL } from 'node:url'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue({
      template: { transformAssetUrls }
    }),
    // https://github.com/vuetifyjs/vuetify-loader/tree/master/packages/vite-plugin#readme
    vuetify({
      autoImport: true,
      styles: {
        configFile: 'src/styles/settings.scss',
      },
    }),
    ViteFonts({
      google: {
        families: [{
          name: 'Roboto',
          styles: 'wght@100;300;400;500;700;900',
        }],
      },
    }),
    VitePWA({
      registerType: 'autoUpdate',
      injectRegister: 'auto',
      workbox: {
        globPatterns: ['**/*.{js,css,html,ico,png,svg,json,vue,txt,woff2}'],        
        runtimeCaching: [
          {
            urlPattern: ({ url }) => url.pathname.startsWith("/api/grid"),
            handler: 'NetworkFirst',
            options: {              
              cacheName: 'api',
              cacheableResponse: {
                statuses: [0, 200]
              }
            }
          },
          // Weird Fix to handle offline mode because assets not in woker precache
          {
            urlPattern: ({ url }) => {
                return url.pathname.startsWith('/')
            },
            handler: 'NetworkFirst',
            options: {
                cacheName: 'build-cache',
                cacheableResponse: {
                    statuses: [0, 200]
                }
            }
        }
        ]
      },
      devOptions: {
        enabled: true
      },
      manifest: {
        name: 'Minesweeper',
        short_name: 'Minesweeper',
        display: 'standalone',
        icons: [
          {
            src: 'icon.png',
            sizes: '512x512',
            type: 'image/png',
            purpose: 'any maskable',          
          }
        ]
      }
    })
  ],
  define: { 'process.env': {} },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
    extensions: [
      '.js',
      '.json',
      '.jsx',
      '.mjs',
      '.ts',
      '.tsx',
      '.vue',
    ],
  },
  server: {
    port: 3000,
  },
})

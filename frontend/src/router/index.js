// Composables
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/views/About.vue'),
  },
  {
    path: '/game',
    name: 'Minesweeper',
    component: () => import('@/views/Game.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router

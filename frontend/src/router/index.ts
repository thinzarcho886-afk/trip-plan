import { createRouter, createWebHistory, Router } from 'vue-router';
import { LandingRoute } from '../constants/LandingRoute';
import { Role } from '../constants/Role';
import { useAuthStore } from '../store/auth';
import { routes } from './routes';
import NProgress from 'nprogress';

NProgress.configure({
  easing: 'ease',
  speed: 500,
  trickleSpeed: 300,
  showSpinner: false,
  template: `<div class="bar" style="height: 3px; background:rgb(248, 162, 50)" role="bar"><div class="peg"></div></div><div class="spinner" role="spinner"><div class="spinner-icon"></div></div>`,
});

const router: Router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// router.beforeEach((to, from, next) => {
//   // start route progress bar loading
//   NProgress.start();

//   const authStore = useAuthStore();

//   if (to.matched.some((record) => record.meta.requiresAuth)) {
//     if (!authStore.isAuth) {
//       next({
//         name: 'Login',
//         query: { redirect: to.fullPath },
//       });
//     } else {
//       // authed
//       const allowedRoles = to.meta?.allowedRoles as Role[];

//       if (!allowedRoles || allowedRoles.length == 0) next();
//       else {
//         if (allowedRoles.includes(authStore.userRole)) next();
//         else {
//           if (LandingRoute[authStore.userRole]) {
//             next({ name: LandingRoute[authStore.userRole] });
//           } else {
//             next({ path: '/' });
//           }
//         }
//       }
//     }
//   } else if (to.matched.some((record) => record.meta.guest)) {
//     // for login page
//     if (!authStore.isAuth) next();
//     else {
//       if (LandingRoute[authStore.userRole]) {
//         next({ name: LandingRoute[authStore.userRole] });
//       } else {
//         next({ path: '/' });
//       }
//     }
//   } else {
//     next();
//   }
// });

router.beforeEach((to, from, next) => {
  // start route progress bar loading
  NProgress.start();

  const authStore = useAuthStore();

  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!authStore.isAuth) {
      next({
        name: 'LoginPage',
        query: { redirect: to.fullPath },
      });
    } else {
      // authed
      const allowedRoles = to.meta?.allowedRoles as Role[];

      if (!allowedRoles || allowedRoles.length == 0) {
        next();
      } else {
        if (allowedRoles.includes(authStore.userRole)) {
          next();
        } else {
          const targetRouteName = LandingRoute[authStore.userRole];
          if (targetRouteName && to.name !== targetRouteName) {
            next({ name: targetRouteName });
          } else if (to.path !== '/') {
            next({ path: '/' });
          } else {
            next();
          }
        }
      }
    }
  } else if (to.matched.some((record) => record.meta.guest)) {
    // for login page
    if (!authStore.isAuth) {
      next();
    } else {
      const targetRouteName = LandingRoute[authStore.userRole];
      if (targetRouteName && to.name !== targetRouteName) {
        next({ name: targetRouteName });
      } else if (to.path !== '/') {
        next({ path: '/' });
      } else {
        next();
      }
    }
  } else {
    next();
  }
});

router.afterEach(() => {
  // Complete the animation of the route progress bar.
  NProgress.done();
});

export default router;

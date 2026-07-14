import { RouteRecordRaw } from 'vue-router';
import { Role } from '../constants/Role';

export const routeNames = {
  home: 'Home',
  userHome: 'UserHome',
  login: 'Login',
  userList: 'UserList',
  userDetail: 'UserDetail',
  userChangePassword: 'UserChangePassword',
  floorList: 'FloorList',
  floorDetail: 'FloorDetail',
  hostelDetail: 'HostelDetail',
  hostelList: 'HostelList',
  publicMain: 'PublicMain',
  editProfile: 'EditProfile',
  welcomePage: 'WelcomePage',
  registerPage: 'RegisterPage',
  loginPage: 'LoginPage',
  changePassword: 'ChangePassword',
  customerList: 'CustomerList',
  customerDetail: 'CustomerDetail'
};

export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('../layouts/blank/Blank.vue'),
    children: [
      {
        path: '',
        name: routeNames.publicMain,
        component: () =>
          import(
            /* webpackChunkName: "publicMain" */ '../views/PublicMain.vue'
          ),
      },
      {
        path: 'login',
        name: routeNames.login,
        component: () =>
          import(/* webpackChunkName: "Login" */ '../views/Login.vue'),
        meta: {
          guest: true,
        },
      },
      {
        path: 'welcomePage',
        name: routeNames.welcomePage,
        component: () =>
          import(
            /* webpackChunkName: "WelcomePage" */ '../views/WelcomePage.vue'
          ),
        meta: {
          guest: true,
        },
      },
      {
        path: '/changePassword',
        name: routeNames.changePassword,
        component: () =>
          import(
            /* webpackChunkName: "ChangePassword" */ '../views/changePassword.vue'
          ),
        meta: {
          requiresAuth: true,
        },
      },

      {
        path: 'edit-profile',
        name: routeNames.editProfile,
        component: () =>
          import(
            /* webpackChunkName: "EditProfile" */ '../views/EditProfile.vue'
          ),
        meta: {},
      },
      {
        path: 'registerPage',
        name: routeNames.registerPage,
        component: () =>
          import(
            /* webpackChunkName: "RegisterPage" */ '../views/RegisterPage.vue'
          ),
        meta: {
          guest: true,
        },
      },
      {
        path: 'loginPage',
        name: routeNames.loginPage,
        component: () =>
          import(/* webpackChunkName: "LoginPage" */ '../views/LoginPage.vue'),
        meta: {
          guest: true,
        },
      },
      {
        path: '/changePassword',
        name: 'routeNames.changePassword',
        component: () =>
          import(
            /* webpackChunkName: "ChangePassword" */ '../views/changePassword.vue'
          ),
        meta: { requiresAuth: true },
      },
      {
        path: 'edit-profile',
        name: routeNames.editProfile,
        component: () =>
          import(
            /* webpackChunkName: "EditProfile" */ '../views/EditProfile.vue'
          ),
        meta: {},
      },
      {
        path: 'about',
        name: 'routeNames.aboutUsPage',
        component: () =>
          import(
            /* webpackChunkName: "aboutUsPage" */ '../views/aboutUs/AboutUsPage.vue'
          ),
        meta: { requiresAuth: false },
      },
    ],
  },
  {
    path: '/admin',
    component: () => import('../layouts/default/Default.vue'),
    children: [
      {
        path: '',
        name: routeNames.home,
        component: () =>
          import(/* webpackChunkName: "Home" */ '../views/Home.vue'),
        meta: {
          requiresAuth: true,
          allowedRoles: [], // optional, specifiy for more restrict
        },
      },
      {
        path: 'users',
        name: routeNames.userList,
        component: () =>
          import(
            /* webpackChunkName: "UserList" */ '../views/user/UserList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN], // optional, specifiy for more restrict
        },
      },

      {
        path: 'users/:id',
        name: routeNames.userDetail,
        component: () =>
          import(
            /* webpackChunkName: "userDetail" */ '../views/user/UserDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN], // optional, specifiy for more restrict
        },
      },
      {
        path: 'users/:id/change-password',
        name: routeNames.userChangePassword,
        component: () =>
          import(
            /* webpackChunkName: "userChangePassword" */ '../views/user/UserChangePassword.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN], // optional, specifiy for more restrict
        },
      },
       {
        path: 'hostels/:id',
        name: routeNames.hostelDetail,
        component: () =>
          import(
            /* webpackChunkName: "hostelDetail" */ '../views/hostels/HostelDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'customer',
        name: routeNames.customerList,
        component: () =>
          import(
            /* webpackChunkName: "customerList" */ '../views/customer/CustomerList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'customer/:id',
        name: routeNames.customerDetail,
        component: () =>
          import(
            /* webpackChunkName: "customerDetail" */ '../views/customer/CustomerDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'hostels',
        name: routeNames.hostelList,
        component: () =>
          import(
            /* webpackChunkName: "hostelList" */ '../views/hostels/HostelList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
    ],
  },
];

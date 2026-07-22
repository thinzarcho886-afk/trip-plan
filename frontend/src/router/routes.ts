import { RouteRecordRaw } from 'vue-router';
import { Role } from '../constants/Role';
import HotelDetail from '../views/hotel/HotelDetail.vue';
import PackageList from '../views/packaged/PackageList.vue';
import BookingDetail from '../views/booking/BookingDetail.vue';

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
  customerDetail: 'CustomerDetail',
  destinationList:'DestinationList',
  durationList:'DurationList',
  durationDetail:'DurationDetail',
  bookingList:'BookingList',
  bookingDetail:'BookingDetail',
  paymentList:'PaymentList',
  paymentDetail:'PaymentDetail',
  destinationDetail: 'DestinationDetail',
  busList: 'BusList',
  busDetail: 'BusDetail',
  packageList: 'PackageList',
  packageDetail:'PackageDetail',
  hotelList:'HotelList',
  hotelDetail:'HotelDetail',
  busTypeList:'BusTypeList',
  busTypeDetail:'BusTypeDetail',
  transportList: 'TransportList',
  transportDetail: 'TransportDetail',
  packagePublicList:'PackagePublicList',
  transportPublicList: 'TransportPublicList',
  busPublicList: 'BusPublicList',
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
        path: '/public-hotels',
        name: 'routeNames.hotelPublicList',
        component: () => import(/* webpackChunkName: "hostelPublicList" */ '../views/hotel/HotelPublicList.vue'),
        meta: { requiresAuth: true }
      },
      {
    path: '/bus-public-list',
    name: 'BusPublicList', // 👈 Error ဖြစ်စေသော နာမည်နှင့် အတိအကျ တူရပါမည်
    component:  () => import(/* webpackChunkName:"BusPublicList" */'../views/bus/BusPublicList.vue'),
    meta: {
      public: true, // Public Access ပေးထားပါက
    },
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
      {
        path: '/public-transports',
        name: 'routeNames.transportPublicList',
        component: () => import(/* webpackChunkName: "transportPublicList" */ '../views/transport/TransportPublicList.vue'),
        meta: { requiresAuth: true }
      },
      {
    path: '/package-detail/:id',
    name: routeNames.packagePublicList,
    component: () => import(/* webpackChunkName: "packagePublicList" */ '../views/packaged/PackagePublicList.vue'),
    meta: { requiresAuth: true }
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
        path: 'payment',
        name: routeNames.paymentList,
        component: () =>
          import(
            /* webpackChunkName: "paymentList" */ '../views/payment/PaymentList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'payment/:id',
        name: routeNames.paymentDetail,
        component: () =>
          import(
            /* webpackChunkName: "paymentDetail" */ '../views/payment/PaymentDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'booking',
        name: routeNames.bookingList,
        component: () =>
          import(
            /* webpackChunkName: "bookingList" */ '../views/booking/BookingList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'booking/:id',
        name: routeNames.bookingDetail,
        component: () =>
          import(
            /* webpackChunkName: "bookingDetail" */ '../views/booking/BookingDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
       {
        path: 'destination',
        name: routeNames.destinationList,
        component: () =>
          import(
            /* webpackChunkName: "destinationList" */ '../views/destination/DestinationList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'duration',
        name: routeNames.durationList,
        component: () =>
          import(
            /* webpackChunkName: "durationList" */ '../views/duration/DurationList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'duration/:id',
        name: routeNames.durationDetail,
        component: () =>
          import(
            /* webpackChunkName: "durationList" */ '../views/duration/DurationDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'busTypes/:id',
        name: routeNames.busTypeDetail,
        component: () =>
          import(
            /* webpackChunkName: "busTypeDetail" */ '../views/busType/BusTypeDetail.vue'),
             meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
          },
        {
        path: 'destination/:id',
        name: routeNames.destinationDetail,
        component: () =>
          import(
            /* webpackChunkName: "destinationDetail" */ '../views/destination/DestinationDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'bus',
        name: routeNames.busList,
        component: () =>
          import(
            /* webpackChunkName: "busList" */ '../views/bus/BusList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'bus/:id',
        name: routeNames.busDetail,
        component: () =>
          import(
            /* webpackChunkName: "busDetail" */ '../views/bus/BusDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
       {
        path: 'busType',
        name: routeNames.busTypeList,
        component: () =>
          import(
            /* webpackChunkName: "busTypeList" */ '../views/busType/BusTypeList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'busType/:id',
        name: routeNames.busTypeDetail,
        component: () =>
          import(
            /* webpackChunkName: "busTypeDetail" */ '../views/busType/BusTypeDetail.vue'
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
      {
        path: 'package',
        name: routeNames.packageList,
        component: () =>
          import(
            /* webpackChunkName: "packageList" */  '../views/packaged/PackageList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'package/:id',
        name: routeNames.packageDetail,
        component: () =>
          import(
            /* webpackChunkName: "packageDetail" */  '../views/packaged/PackageDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },

      {
        path: 'hotel',
        name: routeNames.hotelList,
        component: () =>
          import(
            /* webpackChunkName: "hotelList" */  '../views/hotel/HotelList.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
      {
        path: 'hotel/:id',
        name: routeNames.hotelDetail,
        component: () =>
          import(
            /* webpackChunkName: "hotelDetail" */  '../views/hotel/HotelDetail.vue'
          ),
        meta: {
          requiresAuth: true,
          allowedRoles: [Role.SYSADMIN, Role.ADMIN],
        },
      },
    ],
  },
];

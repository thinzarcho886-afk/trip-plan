import { 
  mdiHome, 
  mdiAccount, 
  mdiAccountGroup,
  mdiHomeAnalytics,
  mdiBus,             
  mdiBusDoubleDecker, 
  mdiNavigation,      
  mdiClockOutline,    
  mdiGift,            
  mdiBookMarker,      
  mdiCreditCard       
} from '@mdi/js';
import { RouteLocationNamedRaw } from 'vue-router';
import { Role } from './Role';
import { routeNames } from '../router/routes';

export interface Menu {
  icon: string;
  name: string;
  to?: RouteLocationNamedRaw;
  forRole: Role[];
  children?: Menu[];
}

export const menus: Menu[] = [
  {
    icon: mdiHome,
    name: 'Home',
    to: { name: routeNames.home },
    forRole: [Role.SYSADMIN],
  },
  {
    name: 'Customer',
    icon: mdiAccountGroup,
    to: { name: routeNames.customerList },
    forRole: [Role.SYSADMIN],
  },
  {
    name: 'User',
    icon: mdiAccount,
    to: { name: routeNames.userList },
    forRole: [Role.SYSADMIN],
  },
  {
    name: 'Hotels',
    icon: mdiHomeAnalytics,
    to: { name: routeNames.hotelList },
    forRole: [Role.SYSADMIN, ],
  },
  {
    name: 'Bus',
    icon: mdiBus,
    to: { name: routeNames.busList }, 
    forRole: [Role.SYSADMIN, ],
  },
  {
    name: 'Bus Types',
    icon: mdiBusDoubleDecker,
    to: { name: routeNames.busTypeList }, 
    forRole: [Role.SYSADMIN],
  },
  {
    name: 'Destination',
    icon: mdiNavigation,
    to: { name: routeNames.destinationList }, 
    forRole: [Role.SYSADMIN, ],
  },
  {
    name: 'Duration',
    icon: mdiClockOutline,
    to: { name: routeNames.durationList }, 
    forRole: [Role.SYSADMIN,],
  },
  {
    name: 'Package',
    icon: mdiGift,
    to: { name: routeNames.packageList }, 
    forRole: [Role.SYSADMIN, ],
  },
  {
    name: 'Booking',
    icon: mdiBookMarker,
    to: { name: routeNames.bookingList }, 
    forRole: [Role.SYSADMIN,],
  },
  {
    name: 'Payment',
    icon: mdiCreditCard,
    to: { name: routeNames.paymentList }, 
    forRole: [Role.SYSADMIN, ],
  },
];

export const getMenus = (role: Role): Menu[] => {
  const roleMenu = menus.filter((menu) => menu.forRole.includes(role));

  return roleMenu.map((menu) => {
    const __menu = { ...menu };

    if (__menu.children && __menu.children.length > 0)
      __menu.children = __menu.children.filter((childMenu) =>
        childMenu.forRole.includes(role),
      );

    return __menu;
  });
};

export const getCurrentMenu = (routeName: any, menus: Menu[]) => {
  for (let index = 0; index < menus.length; index++) {
    const menu = menus[index];
    if (
      menu.to?.name == routeName ||
      (menu.children &&
        menu.children.find((menu) => menu.to?.name == routeName)) ||
      (menu.children &&
        menu.children.find(
          (menu) =>
            menu.to?.name?.toString().replace(/List/, 'Detail') == routeName,
        ))
    )
      return menu;
  }

  return undefined;
};
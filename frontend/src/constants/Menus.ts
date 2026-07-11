import { mdiHomeAnalytics } from '@mdi/js';
import { RouteLocationNamedRaw } from 'vue-router';
import { Role } from './Role';

import { mdiAccount, mdiHome } from '@mdi/js';
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
    forRole: [Role.SYSADMIN, Role.OWNER],
  },

  {
    name: 'User',
    icon: mdiAccount,
    to: { name: routeNames.userList },
    forRole: [Role.SYSADMIN],
  },
   {
    name: 'Customer',
    icon: mdiAccount,
    to: { name: routeNames.customerList },
    forRole: [Role.SYSADMIN],
  },
 

  {
    name: 'Hostels',
    icon: mdiHomeAnalytics,
    to: { name: routeNames.hostelList },
    forRole: [Role.SYSADMIN, Role.OWNER],
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

import { Role } from './Role';

export const LandingRoute: Record<Role, string> = {
  [Role.SYSADMIN]: 'Home',
  [Role.ADMIN]: 'Home',
  [Role.OPERATION]: 'Home',
};

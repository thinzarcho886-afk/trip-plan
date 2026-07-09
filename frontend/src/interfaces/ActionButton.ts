import { RouteLocationNamedRaw } from 'vue-router';

export interface ActionButton {
  icon: string;
  label: string;
  to?: RouteLocationNamedRaw;
  onClick?: Function;
  color: string;
  useLoading?: boolean;
  useDisabled?: boolean;
}

# Project and Components

Note: WIP

## Components

Component: `ListDataTable`

Props:

`headers`: describes the columns of the table. Each contains properties such as `title`, `key`, `sortable`, and `align`. These properties are used by Vuetify datatable.

`apiResource`: represents the URL of the API endpoin. The endpoint is defined in the `api/resources/` directory.

`apiParams`: contains the parameters that are used by the API to filter the data returned.

`responseKey`: represents the key in the API response object that contains the array of data to be displayed.

`defaultSort`: specifies the default sorting for the table. Example `{ key: 'createdBy', order: 'asc' }`

The `ListDataTable` component fetches the data from the API endpoint defined in `apiResource` using the filter parameters defined in `apiParams`. The component then renders the data in a table format using the Vuetify datatable. The headers of the table are defined in the `headers` prop, and the data to be displayed is extracted from the API response using the `responseKey` prop.

Slots:

- Vuetify 3 datatable slots

example role column

```vue
<template v-slot:[`item.role`]="{ item }">
  {{ item.roleName }}
</template>
```

Example:

```vue
<template>
  <ListDataTable
    :headers="headers"
    :apiResource="apiResource"
    :apiParams="apiParams"
    :responseKey="responseKey"
    :defaultSort="defaultSort"
  >
    <template v-slot:[`item.role`]="{ item }">
      {{ item.roleName }}
    </template>
  </ListDataTable>
</template>

<script setup>
import { ref } from "vue";
import { userApiResource } from "api/resources/userResource";

const headers = [
  {
    title: "Username",
    key: "username",
    align: "start",
    sortable: true,
  },
  { title: "Role", key: "role" },
  { title: "Status", key: "status" },
];
const apiResource = userApiResource.getList;
const responseKey = "userList";
const defaultSort = [{ key: "createdBy", order: "asc" }];
</script>
```

---

Component: `List`

Props:

`title`: will be displayed as the title of the page. If no title is provided, the default title will be "List".

`breadcrumbs`: representing the breadcrumbs to be displayed at the top of the page. Each in the array should have a `title` property. If no breadcrumbs are provided, the component will use the current route's full path to generate breadcrumbs.

`actions`: representing the actions to be displayed at the top of the page. Each in the array should have a `label` (button text), `color` (the button color), `to` (the router link to navigate to when the button is clicked), `onClick` (a callback function to be called when the button is clicked), and `icon` (the icon to display on the button).

`filters`: representing the filter to be displayed at the app bar. The object should have a `component` and `onSearch`, which is a callback function to be called when the user performs a search.

Slots:

`actions`: can overwrite default action btns

---

Component: `Detail`

Props:

`title`: will be displayed as the title of the page. If no title is provided, the default title will be "List".

`breadcrumbs`: representing the breadcrumbs to be displayed at the top of the page. Each in the array should have a `title` property. If no breadcrumbs are provided, the component will use the current route's full path to generate breadcrumbs.

`actions`: representing the actions to be displayed at the top of the page. Each in the array should have a `label` (button text), `color` (the button color), `to` (the router link to navigate to when the button is clicked), `onClick` (a callback function to be called when the button is clicked), `icon` (the icon to display on the button), `useDisabled`: (indicating whether the button should be disabled or not, used with the `isFormValid` computed property), and
`useLoading` (indicating whether the button should show a loading indicator or not, used with the `isLoading` computed property).

`loading`: indicating whether the component is currently in a loading state or not.

`error`: indicating whether an error occurred while processing the form or not.

`message`: containing the error message to be displayed in case an error occurred. This prop is used with the `error` prop to display the error message.

`formValid`: indicating whether the form is currently valid or not. This property is used in with the `useDisabled` property of the actions prop to enable/disable action buttons based on the validity of the form.

Slots:

`actions`: overwrite default action btns

---

## Usage

Id filter in search

simple example;
Pre-created picker components can be used

```vue
<template>
  <v-select
    :items="clientList"
    v-model="searchParams.client"
    hide-details
    density="compact"
    placeholder="Client"
    item-value="id"
    item-title="name"
    return-object
    @update:modelValue="
      ($event) => {
        searchParams.clientId = $event?.id;
        searchParams.client = $event?.name;
      }
    "
  ></v-select>
</template>
<script>
export default {
  data: () => ({
    searchParams: { clientId: "", client: null },
    clientList: [
      { id: 1, name: "Client 1" },
      { id: 2, name: "Client 2" },
      { id: 3, name: "Client 3" },
    ],
  }),
};
</script>
```

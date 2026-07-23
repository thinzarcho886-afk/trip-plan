<template>
  <span>{{ formattedDateTime }}</span>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';

const props = defineProps<{
  milliseconds?: number | null;
}>();

const { locale } = useI18n();

const toMyanmarNumber = (num: number | string): string => {
  const englishDigits = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
  const myanmarDigits = ['၀', '၁', '၂', '၃', '၄', '၅', '၆', '၇', '၈', '၉'];
  return num
    .toString()
    .split('')
    .map((char) => {
      const index = englishDigits.indexOf(char);
      return index !== -1 ? myanmarDigits[index] : char;
    })
    .join('');
};

const formattedDateTime = computed(() => {
  if (!props.milliseconds) return '';
  const date = new Date(props.milliseconds);

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');

  const hours24 = date.getHours();
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  // အင်္ဂလိပ်အတွက် AM/PM
  const ampm = hours24 >= 12 ? 'PM' : 'AM';
  let hours12 = hours24 % 12;
  hours12 = hours12 ? hours12 : 12;
  const strHours = String(hours12).padStart(2, '0');

  // English Format (ဥပမာ - 2026-07-21 (PM) 03:30:15)
  const englishFormatted = `${year}-${month}-${day} (${ampm}) ${strHours}:${minutes}:${seconds}`;

  if (locale.value === 'mm') {
    let mmTimeLabel = '';

    // သတ်မှတ်ထားသော အချိန်အပိုင်းအခြားများ
    if (hours24 >= 0 && hours24 < 12) {
      mmTimeLabel = 'မနက်'; // 12am - 11:59am
    } else if (hours24 >= 12 && hours24 < 15) {
      mmTimeLabel = 'နေ့လည်'; // 12pm - 3pm (12:00 - 14:59)
    } else if (hours24 >= 15 && hours24 < 18) {
      mmTimeLabel = 'ညနေ'; // 3pm - 6pm (15:00 - 17:59)
    } else {
      mmTimeLabel = 'ည'; // 6pm - 11:59pm (18:00 - 23:59)
    }

    // ရက်စွဲ၊ အချိန်နှင့် မြန်မာအညွှန်းကို မြန်မာဂဏန်းဖြင့် ပုံဖော်ခြင်း
    const datePart = toMyanmarNumber(`${year}-${month}-${day}`);
    const timePart = toMyanmarNumber(`${strHours}:${minutes}:${seconds}`);

    // ဥပမာ - ၂၀၂၆-၀၇-၂၁ (မနက်) ၀၉:၃၀:၁၅
    return `${datePart} (${mmTimeLabel}) ${timePart}`;
  }

  return englishFormatted;
});
</script>
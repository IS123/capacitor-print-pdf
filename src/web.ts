import { WebPlugin } from '@capacitor/core';

import type { PrintPdfPlugin } from './definitions';

export class PrintPdfWeb extends WebPlugin implements PrintPdfPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

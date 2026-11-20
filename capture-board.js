const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch();  // Or 'firefox' or 'webkit'.
  const page = await browser.newPage({
    viewport: {width: 1920, height: 1080 },
    colorScheme: 'dark'
  });
  
  await page.goto('https://github.com/users/Ashar13546/projects/1/views/2'), { waitUntil: 'networkidle'};

  await page.waitForTimeout(3000);
  await page.screenshot({ path: 'scrumboard.png'});
  // other actions...
  await browser.close();
})();

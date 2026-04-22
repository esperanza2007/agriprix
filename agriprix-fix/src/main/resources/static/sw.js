// AgriPrix — Service Worker (PWA offline)
const CACHE_NAME = 'agriprix-v1';

// Ressources à mettre en cache au premier chargement
const STATIC_ASSETS = [
    '/',
    '/css/agriprix.css',
    '/manifest.json'
];

// Installation : mise en cache des ressources statiques
self.addEventListener('install', event => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => cache.addAll(STATIC_ASSETS))
            .then(() => self.skipWaiting())
    );
});

// Activation : suppression des anciens caches
self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys().then(keys =>
            Promise.all(keys.filter(k => k !== CACHE_NAME).map(k => caches.delete(k)))
        ).then(() => self.clients.claim())
    );
});

// Fetch : stratégie Network-First pour les pages, Cache-First pour les assets
self.addEventListener('fetch', event => {
    const url = new URL(event.request.url);

    // Assets statiques (CSS, JS, images) : Cache-First
    if (url.pathname.startsWith('/css/') || url.pathname.startsWith('/js/') || url.pathname.startsWith('/icons/')) {
        event.respondWith(
            caches.match(event.request).then(cached => cached || fetch(event.request))
        );
        return;
    }

    // API JSON : réseau uniquement (données en temps réel)
    if (url.pathname.startsWith('/api/')) {
        event.respondWith(fetch(event.request));
        return;
    }

    // Pages HTML : Network-First avec fallback sur cache
    event.respondWith(
        fetch(event.request)
            .then(response => {
                const copy = response.clone();
                caches.open(CACHE_NAME).then(cache => cache.put(event.request, copy));
                return response;
            })
            .catch(() => caches.match(event.request))
    );
});

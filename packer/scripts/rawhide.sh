dnf -y install fedora-repos-rawhide

rm -f /etc/yum.repos.d/fedora.repo  /etc/yum.repos.d/fedora-updates.repo /etc/yum.repos.d/fedora-updates-testing.repo
sed -rie 's/(enabled)=0/\1=1/'  /etc/yum.repos.d/fedora-rawhide.repo
dnf -y update
dnf -y --releasever=rawhide --allowerasing distro-sync

dnf -y install autoconf automake bison byacc clang cpp curl flex gcc gdb git glibc-devel libgcrypt-devel libtool libtool-ltdl-devel m4 make nc pkgconfig redhat-rpm-config strace tar valgrind

dnf -y install ganglia-devel gpsd-devel gtk2-devel hiredis-devel iproute-devel iptables-devel java-1.8.0-openjdk-devel java-devel jpackage-utils libatasmart-devel libcap-devel libcurl-devel libdbi-devel libesmtp-devel libmemcached-devel libmnl-devel libmodbus-devel libnotify-devel liboping-devel libpcap-devel librabbitmq-devel libsigrok-devel libstatgrab-devel libudev-devel libvirt-devel libxml2-devel lm_sensors-devel lvm2-devel mosquitto-devel mysql-devel net-snmp-devel OpenIPMI-devel openldap-devel owfs-devel perl-ExtUtils-Embed postgresql-devel protobuf-c-devel python-devel rrdtool-devel varnish-libs-devel xmms-devel yajl-devel # nut-devel

mkdir -p /opt/jenkins
ln -s /usr/lib/jvm/jre-1.8.0/bin/java /opt/jenkins/
